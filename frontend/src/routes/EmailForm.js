import React, { useState } from 'react'
import { Button, Checkbox, Form, Input } from 'semantic-ui-react'
import SendGmailClass from '../components/Gmail/SendGmailClass'
import DropDown from '../components/DropDown';
import { withRouter} from 'react-router-dom';
//import infoService from '../services/info'
//import axios from 'axios'


// ehkä oliskon parempi parametreina välittää tiedot
const EmailForm = ({props, token, socketId, devices,info, func }) => {


  console.log("#############################")
  const [dataSetName, setDataSetName] = useState('')
  const [email, setEmail] = useState('')


  console.log("Emailform  "+JSON.stringify(info)+" "+info.port)

  let backend=info.ip_address
  let port=info.port
  
  //const token = 
  //const socketId = '1234567890'

  //  console.log("EmailForm token=" + token+" socketId="+socketId);

  //const InputExampleLabeled = () => <Input label='http://' placeholder='mysite.com' />


  // lue configista kuten muutkin
  let secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b"

  // saadaan laitetiedoista eli se pitisi suorittaa ekana
  //let coords="62.8376,27.6477"  

  let coords = '%s'

  let exclude = "currently,minutely,daily,alerts,flags"
  let sUrl = "https://api.darksky.net/forecast/" + secretKey + "/" + coords + "?units=si&exclude=" + exclude;

  console.log(sUrl);

  const sUrlInField = "api.darksky.net/forecast/" + secretKey + "/" + coords + "?units=si&exclude=" + exclude;

  //const [sUrlInField, setSUrlInField] = useState(_sUrlInField)


  const sgc = new SendGmailClass();



  // tarvitaanko
  /* 
  useEffect(() => {
    
      //  sgc.init();  
    //    init()
    
      }, [])
  */

  /*
 const queryAddress = () => {

  const request = axios.get('/info' + "/queryAddress")
  //const request = axios.get('http://localhost:3001/address') //'' + "/address")

  return request.then(response => response.data)
  
}*/
//let backend=info.ip_address
//let port=info.port


  
  const onClickHandlexx = async (event) => {

    /*
    let ret=await queryAddress().then(data => {
      
      console.log("data="+data)
      return data
    })*/


    
/*
    let ret = await infoService
      .queryAddress().then(data => {

        console.log("data=" + JSON.stringify(data))
        return data
      })
      */
    /*
    let ret=await dataService
    .queryAddress().then(data => {
      
      console.log("data="+data)
      return data
    })
    */

    // api/datas annetaan path:ksi
    //console.log("ret=" + ret)
    //backend=ret.ip_address
    //port=ret.port
    

  }

  const onClickHandle = (event) => {

    // 
    
    console.log("EmailForm token=" + token + " socketId=" + socketId);


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
        Title: "TESTI",
        Token: token,
        IP: info.ip_address,
        URL:info.url,
        Path: './api/datas',
        Port:info.port,
        SocketId: socketId,
        Source: sUrl
      }


    }
    /*
    String secretKey = "ea8a7cd6a76ab7136502dfe91fde6f7b";
            String coords="62.8376,27.6477";
    */


    console.log("¤¤¤¤¤¤¤DATA ¤¤¤¤¤¤ " + JSON.stringify(data))
    // pois völiaikaisesti
    sgc.sendGmail(data);

    //showMessage-funktio App.js:stä saadaan parametrina 
    // pois völiaikaisesti
    //func(true)
    
    //this.props.history.push("/")
  }

  //

  const handleNameChange = (event) => {
    setDataSetName(event.target.value)
    console.log(dataSetName)
  }
  const handleChange = (event) => {
    //  this.setState({value: event.target.value});
  }



  console.log("devices[0] =" + devices[0].name)

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

  /*
  const options=[{
    key:devices[0].name
  }
  
  ]*/


  const setDevice = (email) => {

    setEmail(email)
    //console.log("DEVICE= "+device)

    //console.log("Device="+device)
  }

  const divStyleRow = {
    display: 'flex',
    flexdirection: 'row'
  };

  //const backend = "savoniaht.herokuapp.com dssdsdssssssd"
  //const port = "3001"  // tosin tämä kehitysmoodissa


  return (
    <Form>

      <Form.Field>
        <label>Device name</label>
        <DropDown placeHolder='Select device' options={options} func={setDevice}></DropDown>
      </Form.Field>
      <Form.Field>
        <label>Name for dataset </label>
        <input placeholder='Give a name for dataset' value={dataSetName} onChange={handleNameChange} disabled={false} />
      </Form.Field>
      <Form.Field>
        <label>Device email</label>
        <input placeholder='Give email of the choosen device' value={email} onChange={handleChange} disabled={true} />
      </Form.Field>
      <Form.Field>
        <label>Data source</label>
        <Input label='https://' placeholder='Give URL with params' value={sUrlInField} onChange={handleChange} disabled={true} />
      </Form.Field>
      <Form.Field>
        <label>Token</label>
        <input placeholder={token} value={token} onChange={handleChange} disabled={true} />
      </Form.Field>
      <Form.Field>
        <label>Socket-io.id</label>
        <input placeholder={socketId} />
      </Form.Field>
      <div style={divStyleRow}>
        <Form.Field style={{ width: '50%' }}>
          <label>Backend</label>
          <input value={backend} onChange={handleChange} disabled={true} />
        </Form.Field>
        <Form.Field>
          <label>Port</label>
          <input value={port} onChange={handleChange} disabled={true} />
        </Form.Field>
      </div>
      <Form.Field>
        <Checkbox label='Save dataset' />
      </Form.Field>
      <Form.Field>
        <Checkbox label='Save query as template' />
      </Form.Field>

      <Button primary type='submit' onClick={onClickHandle}>Submit</Button>
    </Form>
  )
}

export default withRouter(EmailForm)
