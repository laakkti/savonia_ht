import React from 'react'
//import gapi from 'gapi-client'

class SendGmailClass extends React.Component {

  // lienee turha tössö tapauksess 
  constructor() {
    super()
    console.log('call init')
    this.init()

    //window.gapi.load('client:auth2', this.initClient);
  }

  /*
  initClient = () => {
    gapi.client.init({
      discoveryDocs: ["https://www.googleapis.com/discovery/v1/apis/drive/v3/rest"],
      clientId: '28376806989-oeo76ft0gut7h538bk373upodmajvakg.apps.googleusercontent.com',
      scope: 'https://www.googleapis.com/auth/gmail.send'
    }).then(function () {
      // do stuff with loaded APIs
      console.log('it worked');
    });
  }*/


  init = () => {

    // kirjautumiseen liittyvää
    let data = require('./data/client_id.json');
    data = JSON.parse(JSON.stringify(data))

    const client_id = data['web'].client_id

    console.log('#1 xxxxxxxxxxxxxxx')

    return window.gapi.load("client:auth2", () => {

      //console.log('#1 INIT= ')
      var id = { client_id: client_id };

      //console.log('#2 INIT= ' + id.client_id)
      //window.
      window.gapi.auth2.init(id);
      //console.log('#3  INIT= ' + id.client_id)
    });

  }


  authenticate = () => {

    return window.gapi.auth2.getAuthInstance()
      .signIn({ scope: "https://mail.google.com/ https://www.googleapis.com/auth/gmail.modify https://www.googleapis.com/auth/gmail.readonly" });

  }


  loadClient = () => {
    return window.gapi.client.load("https://content.googleapis.com/discovery/v1/apis/gmail/v1/rest");
  }

  execute = (data) => {
    /*
        console.log("execute=  "+JSON.stringify(data))
    
        var _data = require('./data/emailAddress.json');
    
        const loadData = JSON.parse(JSON.stringify(_data));
    */

    let item=data.Content;
    let dataString=JSON.stringify(data.Content)
    //dataString=dataString.replace(/&quot;/g,'"');
//    var ip = require("ip");
//    var os = require('os');

//    var tmp = "href " + window.location.href + "  Hostname=" + window.location.hostname + " Pathname=" + window.location.pathname;

    var ret = btoa(
      `From: ${data.From}\r\n` +
      `To: ${data.To}\r\n` +
      `Subject: ${data.Subject}\r\n\r\n` +
      `${dataString}`      
    ).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
    /*
    var ret = btoa(
      `From: ${data.From}\r\n` +
      `To: ${data.To}\r\n` +
      `Subject: ${data.Subject}\r\n\r\n` +
      `{Target: getserveraddress.herokuapp.com,
        Path: test,
        IpAddress: ${ip.address()},
        IpAddress2: ${os.address},
        Tmp: ${tmp},
        Content: ${data.Content}
      }`
    ).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
    */

    // "Authorization":`${token}`,
    // huomaa hipsut erikoiset $ yhteydessä
    var userId = "gtw.mob@gmail.com";
    var token=data.Token
    return window.window.gapi.client.gmail.users.messages.send({
      "userId": `${userId}`,      
      "resource": {
        "raw": `${ret}`
      }
    })
      .then(function (response) {
        // Handle the results here (response.result has the parsed body).
        console.log("Response", response);

      },
        function (err) { console.error("Execute error", err); });
  }

  //sendGmail = async () => {
  sendGmail = async (data) => {

    console.log("#1")
    //let xxx=await this.init()

    //console.log("#1.1 "+xxx)
    if (!window.gapi.auth2.getAuthInstance().isSignedIn.get()) {

      console.log("#2")
      await this.authenticate();
    }
    console.log("#3")
    await this.loadClient();

    console.log("#4" + JSON.stringify(data))
    await this.execute(data);
    console.log("#5")

  }

}

export default SendGmailClass
