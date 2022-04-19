import React from 'react'
import { useState } from 'react'
import { List, Segment, Container, Button } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
import { withRouter } from 'react-router-dom';
import ModalYesNo from '../components/ModalYesNo'

const DataPage = (props) => {

  console.log("PROPS.dataLoaded_on= "+props._dataLoaded) 

  const [openModal, setOpenModal] = useState(false)
  const [dataId, setDataId] = useState(false)

  const descriptionStyle = {
    color: 'grey',
    fontStyle: 'italic',
    fontSize: 10
  }

  const divStyleRow = {
    display: 'flex',
    flexdirection: 'row'
  };

  const handleModalYesNo = (p) => {

    if(p){     
      props.func(dataId, 'delete')   
    }

    setOpenModal(false)

  }


  const handleDelete = (data_id) => {
   
    // talleteetaan varmistusdialogin takia
    setDataId(data_id)
    setOpenModal(true)
  }

  const handleShow = (data_id) => {

    console.log("PROPS.dataLoaded= "+props._dataLoaded) 
    // lataus dialogi ja voisiko täältä timerilla kysellä samaan funktioon eri parametrilla esim. 'check'onko data ladattu

    console.log("#1 show ")
    props.func(data_id, 'show')
    .then(data => {
      
      props.history.push('/ChartPage')
      //console.log("#2 show "+data)
    })
    console.log("#3 show ")

    /*
    console.log("#1  CHECK" )
    props.func(data_id, 'check').then(data => {
      
      console.log("#2  CHECK "+data)
    })
    */
    //console.log("#2  CHECK "+ret+"  ==> "+ret.data)
    //props.func(data_id, 'show')
    //props.history.push('/ChartPage')
    //setDataId(data_id)

    //setOpenModal(true)
  }

  // {/*<div style={{ height: '200px', width: '50%' }}>*/}
  return (

    <>
    <ModalYesNo _icon='trash' _content='Delete dataset' _question='Are you sure?' _open={openModal} func={handleModalYesNo} />
    
    <List divided relaxed size='medium' celled selection>
      {props.datas.map(data =>

        <List.Item key={(data.id).toString()}>
          <List.Icon name='caret right' size='large' verticalAlign='middle' onClick={() => {
            handleShow(data.id)
          }} />
          <List.Content onClick={() => {
            handleShow(data.id)
          }} >
            <List.Header as='a'>{data.dataset}</List.Header>
            <List.Description style={descriptionStyle} as='a'>{data.date} </List.Description>
          </List.Content>
          <List.Icon name='trash' size='large' color='teal' verticalAlign='middle' onClick={() => {
            
            handleDelete(data.id)
            
          }} />
        </List.Item>

      )}
    </List>
    </>
  )
}


export default withRouter(DataPage)
