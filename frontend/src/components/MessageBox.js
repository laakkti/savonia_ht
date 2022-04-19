import React, { useState } from 'react'
import { Message, Icon } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'

const handleDismiss = () => {

}

const MessageBox = ({ message, header, color, _visible, _icon, delay, func }) => {

  if (_visible === false) {

    return null
    
  } else {

    if (delay) {

      console.log("MessgeBox delay")
      setTimeout(() => {

        func(false)
      }, delay)
    }

  }

  return (
    
    <Message icon color={color}>
      
        <Icon name={_icon} />
        <Message.Content>
          <Message.Header>{header}</Message.Header>
          {message}
        </Message.Content>
      

    </Message>
  )
}

export default MessageBox