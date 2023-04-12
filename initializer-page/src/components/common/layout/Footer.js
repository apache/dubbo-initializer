import PropTypes from 'prop-types'
import React from 'react'

const Footer = ({ children }) => (
  <div className='sticky'>
    <div className='colset colset-submit'>
      <div className='left nopadding'>
        <footer className='footer'>
          <div className='footer-container'>
            © {new Date().getFullYear()} Apache Dubbo
            <br />
            This site is supported by
            <br />
            <span>
              <a
                tabIndex='-1'
                target='_blank'
                rel='noopener noreferrer'
                href='https://dubbo.apache.org'
              >
                dubbo.apache.org
              </a>
            </span>
          </div>
        </footer>
      </div>
      {children && (
        <div className='right nopadding'>
          <div className='submit'>{children}</div>
        </div>
      )}
    </div>
  </div>
)

Footer.defaultProps = {
  children: null,
}

Footer.propTypes = {
  children: PropTypes.node,
}

export default Footer
