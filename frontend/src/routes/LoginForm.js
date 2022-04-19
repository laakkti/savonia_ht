import React from 'react'
import { Button, Divider, Form, Grid, Segment } from 'semantic-ui-react'
import { withRouter } from 'react-router-dom';
//import { prependOnceListener } from 'cluster';


const LoginForm = ( props 
/*
  handleSubmit,
  handleUsernameChange,
  handlePasswordChange,
  username,
  password
  */
) => {

  const handleSubmit=(event)=>{
    //event.preventDefault()

    console.log("#2 login handeSubmit ")
     props.handleSubmit().then(success => {
      
      //props.history.push('/ChartPage')
      console.log("#2 login handeSubmit "+success)
      if(success)
      props.history.push('/')
    })

  }
 

  return (
    <Segment placeholder>
      <Grid columns={2} relaxed='very' stackable>
        <Grid.Column>
          <Form onSubmit={() => {
            handleSubmit()
          }} >

            <Form.Input icon='user' iconPosition='left' label='Username' placeholder='Username'
              value={props.username}
              onChange={props.handleUsernameChange} />
            <Form.Input icon='lock' iconPosition='left' label='Password' type='password'
              value={props.password}
              onChange={props.handlePasswordChange} />

            <Button content='Login' primary type="submit" />
          </Form>
        </Grid.Column>

        <Grid.Column verticalAlign='middle'>
          <Button content='Sign up' icon='signup' size='big' />
        </Grid.Column>
      </Grid>

      <Divider vertical>Or</Divider>
    </Segment>

  )
}

export default withRouter(LoginForm)