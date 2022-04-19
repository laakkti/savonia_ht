import React from 'react'
import { Image } from 'semantic-ui-react'

const Image_ = ({_src,_size}) => {

    console.log("Image= "+_src,_size)
    
return(
<Image src={_src} size={_size}/>

)
}

export default Image_
