import React, { useState, useEffect } from 'react'

import { TransitionGroup, CSSTransition } from "react-transition-group";

import {
  BrowserRouter as Router,
  Route, NavLink, Link, Redirect, withRouter
} from 'react-router-dom'

import { Container, List, Menu, Button } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'

import ModalYesNo from './components/ModalYesNo'
import MessageBox from './components/MessageBox'
import MessagePop from './components/MessagePop'
import Tooltip from './components/Tooltip'

import dataService from './services/datas'
import loginService from './services/login'
import usersService from './services/users'
import devicesService from './services/devices'
import infoService from './services/info'

import Home from './routes/Home'
import DataPage from './routes/DataPage'

import Button_ from './components/Button_'
import ChartPage from './routes/ChartPage'
import DevicePage from './routes/DevicePage'
import LoginForm from './routes/LoginForm'
import EmailForm from './routes/EmailForm'

import socketIOClient from "socket.io-client"

import "./components/styles.css";

// koordinaatit saadaan vasta ko sivulla mutta nyt parametreina
//_center={[62.8376, 27.647]}

const Footer = () => {

  const footerStyle = {
    color: 'green',
    fontStyle: 'italic',
    fontSize: 14,
    display: 'flex',
    flexdirection: 'row'
  }


  return (

    <div style={footerStyle}>
      <em style={{ marginRight: '5px', verticalAlign: 'bottom' }}>Digiteknologian harjoitustyö, Timo Laakkonen Savonia AMK 2019 </em>
      <Tooltip text="0407155101" _trigger={<Button primary circular icon='phone' />} ></Tooltip>
    </div>

  )
}

const App = (props) => {


  //const [timer, setTimer] = useState(null)
  const [datas, setDatas] = useState([])
  const [deviceInfo, setDeviceInfo] = useState([])
  const [titles, setTitles] = useState([])

  const [errorMessage, setErrorMessage] = useState(null)

  // käytetään inputin yhteydessä
  const [username, setUsername] = useState('')
  // käytetään inputin yhteydessä
  const [password, setPassword] = useState('')
  const [user, setUser] = useState(null)

  // messgeBoxin nkyvyys
  const [msgVisible, setMsgVisible] = useState(false)

  const [errorVisible, setErrorVisible] = useState(false)

  // menun aktivoinit state
  const [activeItem, setActiveteItem] = useState('home')

  const [showModalYesNo, setShowModalYesNo] = useState(false)

  const [devices, setDevices] = useState([])

  // ehkei käyttöä
  const [data, setData] = useState([])

  const [token, setToken] = useState(null)
  const [socketId, setSocketId] = useState(null)
  const [info, setInfo] = useState(null)

  //  const [dataLoaded, setDataLoaded] = useState(false)

  let _token = null

  let timer = null



  useEffect(() => {

// vois olla erillinen metodi
    infoService
      .queryAddress().
      then(data => {
        setInfo(data)
        console.log("data=" + JSON.stringify(data))
      })

    //console.log("xxxinfo=" + JSON.stringify(info))
    //backend=ret.ip_address
    //port=ret.port


    //*********************************/ haetaankin Mongosta
    /*
        let devices = require('./Config/devices.json');
    
        devices = JSON.parse(JSON.stringify(devices))
    
        console.log('ÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖ ' + devices.devices[0].Name)
    */
    /*******************/
    // tee funktio
    devicesService.getAll().then(data => {

      setDevices(data)
      console.log("returnValue= " + JSON.stringify(data))

      console.log(data[0].name)

    })

    

  //  var ip = require('ip');
  //  var os = require('os');


    const baseUrl = "/";
    const socket = socketIOClient(baseUrl);
    socket.emit('client', 'Just took connection');

    socket.on("connected", sessionId => {

      setSocketId(sessionId)


    })


    socket.on("NewData", id => {

      //if(msgVisible){
      //setMsgVisible(false) 

      console.log("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤" + "data received " + msgVisible + "   id " + id + " === " + socketId + "    toksen= " + token);

      //}

      /// pöivitetään otsikot liittyy Dataset pageen
      /*      
            dataService
              .getTitles(user.username).then(data => {
                setTitles(data)
              })
            setMessageProp(true)
      */
    })


    socket.on("Ready2Start", data => {

      console.log("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤" + "data received");

      notifyReady2Start(data);
    })


    // 
    socket.on("FromAPI", data => {
      console.log("xxxxxxxxxxxxxxxxxx " + data);
      notifyFromBackend(data);
      //setMsgVisible(false)
      setMessageProp(true)
    })



    // tee funktio
    const loggedUserJSON = window.localStorage.getItem('loggedNoteappUser')

    if (loggedUserJSON) {
      const user = JSON.parse(loggedUserJSON)
      setUser(user)
      console.log("#1--- " + user);
      console.log("#1 user.token--- " + user.token);

      // sisältäää bearerin 
      _token = dataService.setToken(user.token)

      setToken(_token)

      //********************************** */
      getTitles(user)
    }

    //props.history.push('/login')

  }, [])


  const getTitles= (user) =>{

    dataService
    .getTitles(user.username).then(data => {
      setTitles(data)

      console.log("GET TITLES " + JSON.stringify(data))
    })

  } 


  const signOut = () => {
    /******************************************************/
    setUser(null)
    window.localStorage.removeItem('loggedNoteappUser')
    props.history.push('/login')
    /******************************************************/
  }

  const notifyReady2Start = (data) => {

    let di = JSON.parse(data)

    setDeviceInfo(di)
    console.log("Notify2Start " + di.batLevel); //deviceInfo.batLevel+"  "+deviceInfo.netWorkType)

    //showMessage(false)
    setMessageProp(true)

  }

  const notifyFromBackend = (data) => {

    console.log("NotifyFromBack " + data);
  }


  // nämä liittyy nessageBoxiin, mutta onko olytava state????
  const [color, setColor] = useState('blue')
  //let color='green'
  const [msg, setMsg] = useState(['Wait a moment', 'Data is loading...'])
  const [delay, setDelay] = useState(null)
  const [icon, setIcon] = useState('circle notched')

  const [errorMsg, setErrorMsg] = useState(['Error', 'username or password is incorrect'])


  const login = (user) => {
    setUser(user)
  }


  const padding = { padding: 5 }

  // tämä annetaan messageboxille paramtrien joten timeoitin jälkeen kutsutaan

  const showErrorMessage = (visible) => {
    setErrorVisible(visible)
  }

  const showMessage = (visible) => {

    //setDelay(15000)
    console.log("????????????????????????? " + visible);
    // aiheuttaa renderöinnin 
    if (!visible) {

      console.log("call setMessageProp(false)")
      //setDelay(null)
      setMessageProp(false)
    }

    setMsgVisible(visible)

  }


  let cnt = 0
  const checkQueryReady = () => {

    let param = socketId;

    dataService
      .queryReady(param).then(data => {
        console.log("ReadyOrNot " + data)
        if (data) {

          clearInterval(timer)
          setMessageProp(true)
        }
      })

    // eräänlainen timeOut tms.
    cnt++
    console.log("hello " + cnt)
    if (cnt === 20) {
      clearInterval(timer)
    }
  }

  const handleClick4 = () => {

    const newDevice = {
      name: 'Petonen',
      email: 'gtw.mob2@gmail.com'
    }

    let returnValue = createNewDevice(newDevice)
    console.log("returnValue= " + returnValue)

  }

  const createNewDevice = (newDevice) => {

    return devicesService.createNew(newDevice)
  }

  const handleClick3 = () => {

    devicesService.getAll().then(data => {

      console.log("returnValue= " + JSON.stringify(data))

      console.log(data[0].name)

    })


    /*  
    data.map(data => {
      console.log("returnValue= " + data.Name)
    }*/


  }


  const setMessageProp = (p) => {

    if (p) {

      setColor('green')
      setIcon('thumbs up')
      setMsg(["Good news", "Data is loaded now."])
      setDelay(5000)

    } else {
      // tärkeää asettaa ensi muuten jokin muuu state aiehutta renderöiin ja silloin timerikin aktivoituu kun delay!=null
      setDelay(null)
      setColor('blue')
      setIcon('circle notched')
      setMsg(['Wait a moment', 'Data is loading...'])

    }

  }



  const queryAddress = async () => {

    dataService
      .queryAddress().then(data => {

        console.log("data=" + data)
        return data
      })

  }

  const handleClick = async () => {

    let addressInfo = await queryAddress()
      .then(data => {

        console.log("addressInfo= " + data)
      })

    //console.log("addressInfo2= " +addressInfo)


    //showMessage(true)

  }

  // menunvalinnnan aktivointi
  const handleItemClick = (e, { name, disabled }) => {

    if (name === 'signout')
      setShowModalYesNo(true)


    console.log("TOKEN in handleClick on asettunut jo token=" + token)

    console.log("handleItemClick=" + name + "  " + disabled);
    console.log("datas=" + datas);

    setActiveteItem(name)
  }

  //const handleLogin = async (event) => {
  const handleLogin = async () => {
    //event.preventDefault()
    console.log("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ " + username + "   " + password)

    try {
      const user = await loginService.login({
        username, password
      })

      console.log("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ user " + user.username + "   " + user.data + "   " + JSON.stringify(user))


      window.localStorage.setItem('loggedNoteappUser', JSON.stringify(user))
      dataService.setToken(user.token)
      setUser(user)
      setUsername('')
      setPassword('')

      getTitles(user);
      return true

    } catch (exception) {

      setDelay(2500)
      setErrorVisible(true)
      return false
    }
  }


  // muuta nimi
  const handleRow = async (id, action) => {

    // onkohan async await turha tai haitallinen

    if (action === 'show') {

      // arvohan voi olla jotakin eli check ei toimisi kosks vlmis kun tarkatellaan 
      // tilamuuttujan datas arvoa eli datas.length>1
      console.log("1# GET=======")
      setDatas([])
      await dataService
        .getItem(id).then(data => {
          setDatas(data)
          console.log("2# GET======= " + JSON.stringify(data.content))
          return true

        })
      console.log("3# GET=======")

    } else if (action === 'check') {


      //setDataLoaded(true)
      console.log("CHECK in App.js ======= ");
      return true

    }

    else if (action === 'delete') {

      await dataService
        .deleteItem(id).then(data => {


          // palauttaiskoha 
          console.log("DELETE======= " + JSON.stringify(data.content))

        })

      // pivitetään getTitles ja dialogi viiveellä että data on deletoitu
      await dataService
        .getTitles(user.username).then(data => {
          setTitles(data)
        })


    }


  }


  const onClickHandle2 = (event) => {

    setUser(null)
    window.localStorage.removeItem('loggedNoteappUser')
  }
  /*************************/
  const onClickHandle = (event) => {

    const newUser = {
      username: 'laakkti',
      name: 'Android',
      password: 'savonia19',

    }

    let returnValue = createNewUser(newUser);
    console.log("returnValue= " + returnValue);
  }




  const createNewUser = (newUser) => {

    return usersService.users(newUser);
  }


  const handleModalYesNo = (p) => {

    console.log('heippa ' + p)    
    
    setShowModalYesNo(false)
    if(p){
      signOut()
    }

  }

  const pollingQueryReady = (p) => {

    if (p) {

      showMessage(true)
      timer = setInterval(checkQueryReady, 2000);
    } else {


    }
  }

  return (

    <Container>


      <ModalYesNo _icon='sign out' _content='Logging out' _question='Are you sure?' _open={showModalYesNo} func={handleModalYesNo} />

      {/*<Router>*/}
{/*        <TransitionGroup>
          <CSSTransition
            key='xxx'
            classNames="fade"
            timeout={600}
>*/}

          <div>
            {/*<Menu fixed='top' inverted>*/}
            <Menu inverted >
              <Menu.Item icon='home' name='home' as={Link} to="/" active={activeItem === 'home'} onClick={handleItemClick} />
              <Menu.Item icon='list' name='datasets' as={Link} to="/datas" active={activeItem === 'datasets'} disabled={user === null} onClick={handleItemClick} />
              <Menu.Item icon='chart area' name='chart' as={Link} to="/ChartPage" active={activeItem === 'chart'} disabled={datas.length < 1} onClick={handleItemClick} />
              <Menu.Item icon='phone' name='devices' as={Link} to="/DevicePage" active={activeItem === 'devices'} disabled={user === null} onClick={handleItemClick} />
              <Menu.Item icon='star' name='newdata' as={Link} to="/EmailForm" active={activeItem === 'newdata'} disabled={user === null} onClick={handleItemClick} />
              <Menu.Item icon='sign out' name='signout' active={activeItem === 'signout'} disabled={user === null} position='right' color='blue' onClick={handleItemClick} />
              {/*<Menu.Item icon='sign in' name='login' as={Link} to="/login" active={activeItem === 'login'} disabled={user !== null} position='right' onClick={handleItemClick} />*/}
              <Menu.Item icon='sign in' name='login' as={Link} to="/login" active={activeItem === 'login'} disabled={user !== null} position='right' onClick={handleItemClick} />
            </Menu>
            <Route exact path="/" render={() => <Home onLogin={login} />} />
            {/*<Route exact path="/" render={() => <Home datas={['Timo Laakkonen', 'Savonia AMK', 'Digiteknologia']} />} />*/}
            <Route exact path="/ChartPage" render={() => < ChartPage data={datas} />} />

            <Route exact path="/login" render={() => < LoginForm
              username={username}
              password={password}
              handleUsernameChange={({ target }) => setUsername(target.value)}
              handlePasswordChange={({ target }) => setPassword(target.value)}
              handleSubmit={handleLogin}
            />} />
            <Route exact path="/EmailForm" render={() => < EmailForm token={token} socketId={socketId} devices={devices} info={info} func={pollingQueryReady} />} />

            <Route exact path="/datas" render={() => <DataPage datas={titles} func={handleRow} />} />

            <Route exact path="/DevicePage" render={() => <DevicePage _zoom={11} _text='Iot' socketId={socketId} deviceInfo={deviceInfo} devices={devices} />} />


        </div>
    {/*    </CSSTransition>
        </TransitionGroup>
    */}
        
      {/*</Router>*/}

    <MessagePop header={msg[0]} message={msg[1]} _color={color} _visible={msgVisible} _icon={icon} delay={delay} func={showMessage} />
    <MessageBox header={errorMsg[0]} message={errorMsg[1]} color='red' _visible={errorVisible} _icon='thumbs down' delay={delay} MessageBox func={showErrorMessage} />
    <div>
      {/*      <Button_ _icon='phone' _primary='true' onClick={handleClick} />
      <Button_ _icon='redo' onClick={handleClick3} />
      <Button_ _icon='thumbs up' _primary='true' onClick={handleClick4} />
          */}
      <br />
      <Footer />
    </div>
    </Container >


  )
}

export default withRouter(App)

