import get from 'lodash.get'
import querystring from 'querystring'
import set from 'lodash.set'

import Extend from '../../Extend.json'
import {isInRange, parseReleases, parseVersion} from './Version'

const PROPERTIES_MAPPING_URL = {
  type: 'project',
  language: 'language',
  dubboVersion: 'dubboVersion',
  architecture: 'architecture',
  platformVersion: 'boot',
  packaging: 'meta.packaging',
  jvmVersion: 'meta.java',
  groupId: 'meta.group',
  artifactId: 'meta.artifact',
  name: 'meta.name',
  description: 'meta.description',
  packageName: 'meta.packageName',
  dependencies: 'dependencies',
}

export const getInfo = function getInfo(url) {
  return new Promise((resolve, reject) => {
    fetch(`${url}`, {
      method: 'GET',
      headers: {
        Accept: 'application/vnd.initializr.v2.1+json',
      },
    })
      .then(
        response => response.json(),
        () => {
          reject()
          return null
        }
      )
      .then(data => {
        if (data) {
          resolve(data)
        }
      })
  })
}

export const getShareUrl = values => {
  const props = {}
  Object.keys(PROPERTIES_MAPPING_URL).forEach(key => {
    const key2 = get(PROPERTIES_MAPPING_URL, key)
    const value = get(values, key2)
    if (key !== 'dependencies') {
      set(props, key, value)
    }
  })
  let params = `${querystring.stringify(props)}`
  if (get(values, 'dependencies', []).length > 0) {
    params = `${params}&dependencies=${get(values, 'dependencies').join(',')}`
  }
  return params
}

export const isValidParams = params => {
  return (
    Object.keys(params)
      .map(entry => {
        return !!get(PROPERTIES_MAPPING_URL, entry, null)
      })
      .filter(item => !!item).length > 0
  )
}

export const parseParams = (values, queryParams, lists) => {
  const errors = {}
  const warnings = {}
  if (isValidParams(queryParams)) {
    Object.keys(queryParams).forEach(entry => {
      const key = get(PROPERTIES_MAPPING_URL, entry)
      if (key) {
        const value = get(queryParams, entry, '').toLowerCase()
        switch (key) {
          case 'project':
          case 'language':
          case 'architecture':
          case 'meta.packaging':
          case 'dubboVersion':
          case 'meta.java': {
            const list = get(lists, key, [])
            const res = list.find(a => a.key.toLowerCase() === value)
            if (res) {
              set(values, key, res.key)
            } else {
              const currentValue = list.find(
                a => a.key.toLowerCase() === get(values, key)
              )
              set(warnings, key, {
                value: get(queryParams, entry, ''),
                select: currentValue.text,
              })
            }
            break
          }
          case 'boot': {
            const list = get(lists, key, [])
            const res = list.find(a => a.key.toLowerCase() === value)
            let error = false
            if (res) {
              set(values, key, res.key)
            } else {
              error = true
              let versionMajor = value
              if (versionMajor.indexOf('.x') === -1) {
                versionMajor = get(parseVersion(versionMajor), 'major', '')
              }
              if (versionMajor.indexOf('.x') > -1) {
                const releases = parseReleases(list).filter(
                  release =>
                    release.major.toLowerCase() === versionMajor.toLowerCase()
                )
                if (releases.length > 0) {
                  const release = releases.reduce((p, c) => {
                    if (p.qualify > c.qualify) {
                      return p
                    }
                    if (p.qualify === c.qualify) {
                      if (p.minor > c.minor) {
                        return p
                      }
                    }
                    return c
                  }, releases[0])

                  if (release) {
                    error = false
                    set(values, key, release.version)
                    const currentValue = list.find(
                      a => a.key.toLowerCase() === release.version.toLowerCase()
                    )
                    set(warnings, key, {
                      value: get(queryParams, entry, ''),
                      select: currentValue.text,
                    })
                  }
                }
              }
            }
            if (error) {
              set(errors, 'boot', {
                value: get(queryParams, entry, ''),
              })
            }
            break
          }
          case 'dependencies': {
            const depsWarning = []
            const newVal = value
              .split(',')
              .map(item => {
                const dep = get(lists, 'dependencies').find(
                  d => d.id === item.trim()
                )
                if (dep) {
                  return dep.id
                }
                depsWarning.push(item)
                return null
              })
              .filter(item => !!item)

            if (depsWarning.length > 0) {
              set(warnings, key, {
                value: depsWarning.join(', '),
              })
            }
            set(values, key, newVal)
            break
          }
          default:
            set(values, key, get(queryParams, entry, ''))
        }
      }
    })
  }
  return {
    values,
    errors,
    warnings,
  }
}

export const getLists = json => {
  const deps = []
  get(json, 'dependencies.values', []).forEach(group => {
    group.values.forEach(item => {
      const extend = Extend.find(it => it.id === get(item, 'id', ''))
      const val = {
        id: `${get(item, 'id', '')}`,
        name: `${get(item, 'name', '')}`,
        group: `${group.name}`,
        description: `${get(item, 'description', '')}`,
        versionRange: `${get(item, 'versionRange', '')}`,
        versionRequirement: `${get(item, 'versionRange', '')}`,
        weight: get(extend, 'weight', 50),
      }
      deps.push(val)
    })
  })
  return {
    project: get(json, 'type.values', [])
      .filter(type => type.action === '/starter.zip')
      .map(type => ({
        key: `${type.id}`,
        text: `${type.name}`,
      })),
    language: get(json, 'language.values', []).map(language => ({
      key: `${language.id}`,
      text: `${language.name}`,
    })),
    dubboVersion: get(json, 'dubboVersion.values', []).map(dubboVersion => ({
      key: `${dubboVersion.id}`,
      text: `${dubboVersion.name}`,
    })),
    architecture: get(json, 'architecture.values', []).map(architecture => ({
      key: `${architecture.id}`,
      text: `${architecture.name}`,
      dependencies: get(architecture, 'dependOn', []),
    })),
    boot: get(json, 'bootVersion.values', []).map(boot => ({
      key: `${boot.id}`,
      text: `${boot.name}`,
    })),
    meta: {
      java: get(json, 'javaVersion.values', []).map(java => ({
        key: `${java.id}`,
        text: `${java.name}`,
      })),
      packaging: get(json, 'packaging.values', []).map(packaging => ({
        key: `${packaging.id}`,
        text: `${packaging.name}`,
      })),
    },
    dependencies: deps,
  }
}

export const getDefaultValues = json => {
  return {
    project: get(json, 'type.default'),
    language: get(json, 'language.default'),
    dubboVersion: get(json, 'dubboVersion.default'),
    architecture: get(json, 'architecture.default'),
    boot: get(json, 'bootVersion.default'),
    meta: {
      name: get(json, 'name.default'),
      group: get(json, 'groupId.default'),
      artifact: get(json, 'artifactId.default'),
      description: get(json, 'description.default'),
      packaging: get(json, 'packaging.default'),
      packageName: get(json, 'packageName.default'),
      java: get(json, 'javaVersion.default'),
    },
    share: get(json, 'share.default'),
    dependencies: [],
    availableCount: [],
  }
}

export const getConfig = json => {
  return {
    lists: getLists(json),
    defaultValues: getDefaultValues(json),
  }
}

export const isValidDependency = function isValidDependency(boot, dubbo, dependency) {
  if (!dependency) {
    return false
  }
  return get(dependency, 'versionRange')
          ? isInRange((dependency.id.indexOf('dubbo') === -1 ? boot : dubbo), get(dependency, 'versionRange'))
      : true
}

export const getQueryString = function getQueryString(values, config) {
  const params = querystring.stringify({
    type: get(values, 'project'),
    language: get(values, 'language'),
    architecture: get(values, 'architecture'),
    bootVersion: get(values, 'boot'),
    dubboVersion: get(values, 'dubboVersion'),
    baseDir: get(values, 'meta.artifact'),
    groupId: get(values, 'meta.group'),
    artifactId: get(values, 'meta.artifact'),
    name: get(values, 'meta.name'),
    description: get(values, 'meta.description'),
    packageName: get(values, 'meta.packageName'),
    packaging: get(values, 'meta.packaging'),
    javaVersion: get(values, 'meta.java'),
  })
  let paramsDependencies = get(values, 'dependencies', [])
    .map(dependency => {
      const dep = config.find(it => it.id === dependency)
      return isValidDependency(get(values, 'boot'), get(values, 'dubboVersion'), dep) ? dependency : null
    })
    .filter(dep => !!dep)
    .join(',')
  if (paramsDependencies) {
    paramsDependencies = `&dependencies=${paramsDependencies}`
  }

  return `${params}${paramsDependencies}`
}

export const getProject = function getProject(url, values, config) {
  return new Promise((resolve, reject) => {

    const paramsStr = getQueryString(values, config)

    fetch(`${url}?${paramsStr}`, {
      method: 'GET',
    }).then(
      response => {
        if (response.status === 200) {
          resolve(response.blob())
          return
        }
        reject()
      },
      () => {
        reject()
      }
    )
  })
}
