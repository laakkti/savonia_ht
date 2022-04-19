import React, { useState } from 'react'
import { Message, Icon } from 'semantic-ui-react'
import { Button, Grid, Header, Label, Segment, Portal } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'

const handleDismiss = () => {

}

const MessageBoxPop = ({header,message, _color, _visible,_icon, delay, func }) => {

  let anim

  if (delay!==null) {
  
    console.log("delay="+delay)
    anim=false
    setTimeout(() => {

      //setColor('red')
      func(false)
    }, delay)
  
  }else{

   anim=true
  }

  return (
    <Portal open={_visible}>
      <Segment style={{ left: '40%', position: 'fixed', top: '40%', zIndex: 1000 }}>
        <Message icon color={_color}>

          <Icon name={_icon} loading={anim} />
          <Message.Content>
            <Message.Header>{header}</Message.Header>
            {message}
          </Message.Content>

        </Message>
      </Segment>
    </Portal >
  )

}

export default MessageBoxPop