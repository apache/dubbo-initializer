import get from 'lodash.get'
import React, {useContext, useEffect, useRef, useState} from 'react'
import {CSSTransition, TransitionGroup} from 'react-transition-group'

import {AppContext} from '../../reducer/App'
import {IconCaretDown, IconFeedBack, IconHelp, IconHot, IconInitialzrTutorial} from '../icons'
import {Switch} from '../form'

const QuickLinks = () => {
    const {theme, dispatch, language} = useContext(AppContext)
    const [help, setHelp] = useState(false)
    const wrapper = useRef(null)
    const toggleTheme = () => {
        const newTheme = theme === 'dark' ? 'light' : 'dark'
        dispatch({
            type: 'UPDATE',
            payload: {
                theme: newTheme,
            },
        })
    }
    const toggleLanguage = () => {
        const newLanguage = language === 'en' ? 'zh' : 'en'
        dispatch({
            type: 'UPDATE',
            payload: {
                language: newLanguage,
            },
        })
    }
    useEffect(() => {
        const clickOutside = event => {
            const children = get(wrapper, 'current')
            if (children && !children.contains(event.target)) {
                setHelp(false)
            }
        }
        document.addEventListener('mousedown', clickOutside)
        return () => {
            document.removeEventListener('mousedown', clickOutside)
        }
    }, [setHelp])

    /*
    <li>
        <span className='switch-language'>
          <Switch id='language-switch' isOn={language === 'en'} onChange={toggleLanguage} />
          {language === 'en' ? 'English' : '中文'}
        </span>
    </li>
    */
    return (
        <ul className='quick-links'>
            <li>
               <span className='switch-mode'>
                  <Switch id='theme-switch' isOn={theme === 'dark'} onChange={toggleTheme}/>
                    {theme === 'dark' ? 'Dark' : 'Light'} Theme
                </span>
            </li>
            <li>
                <a
                    target='_blank'
                    rel='noopener noreferrer'
                    href='https://dubbo.apache.org/'
                    tabIndex='-1'
                >
                    <IconInitialzrTutorial/>
                   Visit Dubbo Website
                </a>
            </li>
            <li>
                <a
                    target='_blank'
                    rel='noopener noreferrer'
                    href='https://dubbo.apache.org/zh-cn/contact/books/'
                    tabIndex='-1'
                >
                    <IconHot/>
                    Dubbo E-books
                </a>
            </li>
            <li>
                <a
                    href='/'
                    className='dropdown'
                    tabIndex='-1'
                    onClick={e => {
                        e.preventDefault()
                        setHelp(!help)
                    }}
                    ref={wrapper}
                >
                    <IconHelp/>
                    Help
                    <IconCaretDown className='caret'/>
                </a>

                <TransitionGroup component={null}>
                    {help && (
                        <CSSTransition classNames='nav-anim' timeout={500}>
                            <ul className='dropdown-menu'>
                                <li>
                                    <a
                                        id='ql-help-projects'
                                        target='_blank'
                                        rel='noopener noreferrer'
                                        href='http://github.com/apache/dubbo-initializer'
                                        tabIndex='-1'
                                    >
                                        Apache Dubbo Initializer
                                    </a>
                                </li>
                                <li>
                                    <a
                                        id='ql-help-guides'
                                        target='_blank'
                                        rel='noopener noreferrer'
                                        tabIndex='-1'
                                        href='http://github.com/apache/dubbo'
                                    >
                                        Apache Github Dubbo-java
                                    </a>
                                </li>
                                <li>
                                    <a
                                        id='ql-help-guides'
                                        target='_blank'
                                        rel='noopener noreferrer'
                                        tabIndex='-1'
                                        href='http://github.com/apache/dubbo-go'
                                    >
                                        Apache Github Dubbo-go
                                    </a>
                                </li>
                            </ul>
                        </CSSTransition>
                    )}
                </TransitionGroup>
            </li>
        </ul>
    )
}

export default QuickLinks
