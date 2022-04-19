import React from 'react'
import { useState, useEffect } from 'react'
import { Button, Header, Icon, Modal } from 'semantic-ui-react'

const ModalYesNo = ({_icon,_content,_question, _open, func }) => {

  return (

    <Modal size='mini'
      open={_open}

    >
      <Header icon={_icon} content={_content} />
      <Modal.Content>
        <p>
          {_question}
      </p>
      </Modal.Content>
      <Modal.Actions>
        <Button secondary onClick={() => func(false)}>
          <Icon name='remove' /> No
      </Button>
        <Button primary onClick={() => func(true)}>
          <Icon name='checkmark' /> Yes
      </Button>
      </Modal.Actions>
    </Modal>
  )
}


export default ModalYesNo