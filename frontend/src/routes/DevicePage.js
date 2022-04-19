import React from 'react'
import { useState } from 'react'
import MapView from '../components/MapView'
import Progress_ from '../components/Progress_'
import { Segment, Divider, Grid, Label, Icon, Progress,Form,Button } from 'semantic-ui-react'
import DropDown from '../components/DropDown';
import SendGmailClass from '../components/Gmail/SendGmailClass'

const DevicePage = ({ _zoom, _text, socketId,deviceInfo,devices}) => {

  const [email, setEmail] = useState('')
  
  const sgc = new SendGmailClass();

  let options = []

  devices.map(data => {
    console.log("Device name = " + data.name)
    let item = {
      key: data.name,
      text: data.name,
      value: data.email

    }
    options.push(item)
  })

  const handleChange = (event) => {
    //  this.setState({value: event.target.value});
  }

  const setDevice = (email) => {

    setEmail(email)
    //console.log("DEVICE= "+device)

    //console.log("Device="+device)
  }

  const onClickHandle = (event) => {

    //var ip = require("ip");
    //var os = require('os');

    


    /*
    var _data = require('./data/emailAddress.json');
    const loadData = JSON.parse(JSON.stringify(_data));
*/



    // secret syötetään myös Sourceeen From ei välttämättä tarvita  tai voineen olla mitä vain

    // luetaan jostakin tiedostosta laittten email-osoite, luetaan kuten config.json backendillä,
    // muutta nehän pitää saada backendiltä 
    let data = {

      From: 'gtw.mob@gmail.com',
      To: 'gtw.mob@gmail.com',
      Subject: 'query',
      Content: {
        IP: 'info.address',
        SocketId: socketId,
      }


    }
    /*
    String secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b";
            String coords="62.8376,27.6477";
    */


    console.log("¤¤¤¤¤¤¤DATA ¤¤¤¤¤¤ " + JSON.stringify(data))
    sgc.sendGmail(data);

    //showMessage-funktio App.js:stä saadaan parametrina 
    //func(true)

  }


  if (!deviceInfo.batLevel) {

    return (
      <Form>

        <Form.Field>
          <label>Device name</label>
          <DropDown placeHolder='Select device' options={options} func={setDevice}></DropDown>
        </Form.Field>
        <Form.Field>
          <label>Device email</label>
          <input placeholder='Give email of the choosen device' value={email} onChange={handleChange} disabled={true} />
        </Form.Field>

        <Form.Field>
          <label>Socket-io.id</label>
          <input placeholder={socketId} />
        </Form.Field>

        <Button primary type='submit' onClick={onClickHandle}>Submit</Button>
      </Form>
    )
  }


  let _center = [deviceInfo.location[0], deviceInfo.location[1]]

  const [center, setCenter] = useState(_center)
  const [zoom, setZoom] = useState(_zoom)
  const [text, setText] = useState(_text)

  const progressStyle = {
    backgroundColor: '#C0E2E1',
    width: '50%'
  }

  const divStyleCol = {
    display: 'flex',
    flexdirection: 'column'
  };
  const divStyleRow = {
    display: 'flex',
    flexdirection: 'row'
  };

  let batLevel = {
    value: deviceInfo.batLevel,
    total: 100,
    label: 'Battery level',
    active: true
  }


  let val = batLevel.value

  if (val > 49) {
    batLevel.color = 'green'
  } else if (val <= 49 && val > 29) {
    batLevel.color = 'orange'
  } else {
    batLevel.color = 'red'
  }

  let memFree = deviceInfo.memory[1] - deviceInfo.memory[0]
  let memFreePros = Math.round((deviceInfo.memory[1] - deviceInfo.memory[0]) / deviceInfo.memory[1] * 100)

  let memLevel = {
    value: memFree,
    label: 'Free memory',
    active: true
  }

  val = memFreePros

  if (val > 49) {
    memLevel.color = 'green'
  } else if (val <= 49 && val > 29) {
    memLevel.color = 'orange'
  } else {
    memLevel.color = 'red'
  }




  return (

    <Segment placeholder>
      <Grid columns={2} relaxed='very' stackable>
        <Grid.Column>
          <MapView center={center} zoom={zoom} text={text}></MapView>
        </Grid.Column>

        <Grid.Column verticalAlign='middle'>
          <Segment style={progressStyle} >

            <label style={{ fontSize: '10px' }}>Location</label>

            <div style={divStyleRow}>
              <div style={{ marginRight: '5px' }} ><Label>{deviceInfo.location[0]}</Label></div>
              <div style={{ marginRight: '5px' }} ><Icon size='small' circular color='teal' name='circle' /></div>
              <div style={{ marginRight: '5px' }} ><Label>{deviceInfo.location[1]}</Label></div>

            </div>

            <div style={{ display: 'flex' }}>
              <div style={{ marginRight: '10px' }}>
                <div><label style={{ fontSize: '10px' }} >Network type</label></div>
                <div><Label>{deviceInfo.netWorkType}</Label></div>
              </div>
              <div>
                <div><label style={{ fontSize: '10px' }} >Signal strength</label></div>
                <div><Label>{deviceInfo.signalStrength}</Label></div>
              </div>

              {/*
              <label style={{ fontSize: '10px' }}>Memory usage</label>
              <div style={{ display: 'flex' }}>
              <div style={{ marginRight: '5px' }} ><Label>{deviceInfox.memory[0]}</Label></div>
              <div style={{ marginRight: '5px' }} ><Label color='black'>/</Label></div>
              <div style={{ marginRight: '5px' }} ><Label>{deviceInfox.memory[1]}</Label></div>
             </div>*/}
            </div>

            <div>
              <label style={{ fontSize: '10px' }} >{memLevel.label} ({memLevel.value} /   {deviceInfo.memory[1]})</label>
              <Progress size='medium' percent={memFreePros} color={memLevel.color} progress active={memLevel.active}></Progress>
            </div>

            <div style={{ marginTop: '-20px', marginBottom: '-20px' }}>
              <label style={{ fontSize: '10px' }} >{batLevel.label}</label>
              <Progress size='medium' percent={batLevel.value} progress color={batLevel.color} active={batLevel.active}></Progress>
            </div>
            <div>
              <div><label style={{ fontSize: '10px' }} >Battery °C </label></div>
              <div><Label>{deviceInfo.batTemp}</Label></div>
            </div>

            <div>
              <div><label style={{ fontSize: '10px' }} >CPU °C </label></div>
              <div><Label>{deviceInfo.cpuTemp}</Label></div>
            </div>

          </Segment>
        </Grid.Column>
      </Grid>

      <Divider vertical></Divider>
    </Segment>

  )

}

export default DevicePage