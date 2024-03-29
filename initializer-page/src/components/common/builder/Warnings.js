import get from 'lodash.get'
import React, {useContext} from 'react'

import {IconTimes} from '../icons'
import {InitializrContext} from '../../reducer/Initializr'

function Warnings() {
  const { warnings, dispatch } = useContext(InitializrContext)
  if (Object.keys(warnings).length > 0) {
    return (
      <div className='colset'>
        <div className='left' />
        <div className='right'>
          <div className='warnings'>
            <a
              className='close'
              href='/#'
              onClick={event => {
                event.preventDefault()
                dispatch({
                  type: 'CLEAR_WARNINGS',
                })
              }}
            >
              <IconTimes />
            </a>
            The following attributes could not be handled:
            <ul>
              {get(warnings, 'project') && (
                <li>
                  <strong>{get(warnings, 'project.value')}</strong> is not a
                  valid project type,{' '}
                  <strong>{get(warnings, 'project.select')}</strong> has been
                  selected.
                </li>
              )}
              {get(warnings, 'language') && (
                <li>
                  <strong>{get(warnings, 'language.value')}</strong> is not a
                  valid language,{' '}
                  <strong>{get(warnings, 'language.select')}</strong> has been
                  selected.
                </li>
              )}
                {get(warnings, 'dubboVersion') && (
                    <li>
                        <strong>{get(warnings, 'dubboVersion.value')}</strong> is not a
                        available,{' '}
                        <strong>{get(warnings, 'dubboVersion.select')}</strong> has been
                        selected.
                    </li>
                )}
              {get(warnings, 'boot') && (
                <li>
                  Spring Boot <strong>{get(warnings, 'boot.value')}</strong> is
                  not available, <strong>{get(warnings, 'boot.select')}</strong>{' '}
                  has been selected.
                </li>
              )}
              {get(warnings, 'meta.java') && (
                <li>
                  <strong>{get(warnings, 'meta.java.value')}</strong> is not a
                  valid Java version,{' '}
                  <strong>{get(warnings, 'meta.java.select')}</strong> has been
                  selected.
                </li>
              )}
              {get(warnings, 'meta.packaging') && (
                <li>
                  <strong>{get(warnings, 'meta.packaging.value')}</strong> is
                  not a valid packaging,{' '}
                  <strong>{get(warnings, 'meta.packaging.select')}</strong> has
                  been selected.
                </li>
              )}
              {get(warnings, 'dependencies') && (
                <li>
                  The following dependencies are not supported:{' '}
                  <strong>{get(warnings, 'dependencies.value')}</strong>.
                </li>
              )}
            </ul>
          </div>
        </div>
      </div>
    )
  }
  return <></>
}

export default Warnings
