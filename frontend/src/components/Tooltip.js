import React from 'react'
import { Button, Popup } from 'semantic-ui-react'

const Tooltip = ({text,_trigger}) => (
  <Popup content={text} trigger={_trigger} inverted />
)

export default Tooltip