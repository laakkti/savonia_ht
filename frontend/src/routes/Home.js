import React from 'react'
import { List } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
import Image_ from '../components/Image_'
import idea from '../Images/idea.PNG'
import { withRouter} from 'react-router-dom';

//const Home = ({ datas, func }) => {
  const Home = (props) => {


  const handleClick = (event) => {
  event.preventDefault()
    
  props.history.push('/Login')
  }

  return (
    <div>
      <h2>Savonia digiteknologian harjoitustyö</h2>

      <br></br>
      
      <Image_ _src={idea} _size='huge'></Image_>
     <button onClick={handleClick}>To Login</button>
      {/*<List>
        {datas.map((data, index) =>
          <List.Item key={index}>
            <List.Icon name='user' size='large' verticalAlign='middle' />
            <List.Content>
              <List.Header as='a'>{data}</List.Header>
              
            </List.Content>
          </List.Item>
        )}
        </List>*/}


    </div>
  )
}

export default withRouter(Home)