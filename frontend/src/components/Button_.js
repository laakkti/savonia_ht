import React from 'react'
import { Button } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'

const Button_ = (props) => {
  
  if(props._primary){
  return(
    <div>
      <Button primary circular icon={props._icon} onClick={props.onClick} />
    </div>
  )
  }else{

    return(
      <div>
        <Button secondary circular icon={props._icon} onClick={props.onClick} />
      </div>
      )

  }
}

export default Button_
