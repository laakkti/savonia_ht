import React from 'react'
import {
  BrowserRouter as Router,
  Route, Redirect, withRouter
} from 'react-router-dom'

let Login = (props) => {
  console.log("in Login compoonent");
  const onSubmit = (event) => {
    event.preventDefault()
    props.onLogin('laakkti')
    props.history.push('/')

  }

  return (
    <div>
      <h2>login</h2>
      <form onSubmit={onSubmit}>
        <div>
          username: <input />
        </div>
        <div>
          password: <input type='password' />
        </div>
        <button type="submit">login</button>
      </form>
    </div>
  )
}

export default withRouter(Login)