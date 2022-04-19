import React from 'react'
import { Dropdown } from 'semantic-ui-react'


const DropDown = ({ placeHolder, options, func }) => {

    const handleChange = (event, {value}) => {
        console.log(value);
        let bird_name = event.target.textContent;
        console.log(bird_name);
        func(value)
    }


    return (
        <Dropdown
            placeholder={placeHolder}
            fluid
            selection
            options={options}
            onChange={handleChange}
        />
    )
}
export default DropDown