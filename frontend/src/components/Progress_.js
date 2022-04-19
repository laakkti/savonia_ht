import React from 'react'
import { Progress } from 'semantic-ui-react'

const Progress_ = ({ value,total,color,label,progress,active}) => {
    
    
    
    let _progress
  
    
 // ehdot pitää määritellä ukopuolella tämän komponentin ei tarvii tietää mitään   
 //if(value===66){
    //_progress=<Progress value={value} total={100} progress={progress} label={label} color={color} active={active}/>
    _progress=<Progress value={value} total={total} progress={progress} label={label} color={color} active={active}/>

/*
}else if(value<50 && value>=30){
        _progress=<Progress percent={ value } progress warning>Battery Level</Progress>
    }else if(value<30) {
        _progress=<Progress percent={ value } progress error/>
    }else{
        _progress=<Progress value={value} total={50} progress='value' label='label' color={color} success/>
    }
*/

    return (
        <>
        {_progress}
        </>
        
    )
}
export default Progress_


{/*
import React from 'react'
import { Progress } from 'semantic-ui-react'

const Progress_ = ({ value }) => {
    let _progress
    
    if(value<50 && value>=30){
        _progress=<Progress percent={ value } progress warning/>
    }else if(value<30) {
        _progress=<Progress percent={ value } progress error/>
    }else{
        _progress=<Progress percent={ value } progress success/>
    }

    return (
        <>
        {_progress}
        </>
        
    )
}
export default Progress_
*/}
