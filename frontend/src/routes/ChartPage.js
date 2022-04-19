import React, { useState, useEffect } from 'react'
import Chart from '../components/Chart'
import { Menu } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
/*
import {
  BrowserRouter as Router,
  Route, NavLink, Link, Redirect, withRouter
} from 'react-router-dom'
*/

const ChartPage = ({ data }) => {

  const [activeItem, setActiveteItem] = useState('temperature')
  const [chartData, setChartData] = useState([])
  //const [datas, setDatas] = useState([])

  const datas = data;


  useEffect(() => {

    const defaultItem = 'temperature'
    setActiveteItem(defaultItem)
    getFieldData(datas, [defaultItem, 'time'])


  }, [])


  //setDatas(data)
  const handleItemClick = (e, { name }) => {

    console.log("handleItemClick=" + name);

    //console.log("Activete item");
    setActiveteItem(name)
    getFieldData(datas, [name, 'time'])
  }

  // lisää tämä utilsiin tms.
  const timeConverter = (UNIX_timestamp) => {
    var a = new Date(UNIX_timestamp * 1000);
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var year = a.getFullYear();
    //var month = months[a.getMonth()];
    var month = a.getMonth();
    var date = a.getDate();
    var hour = a.getHours();
    if (hour < 10)
      hour = "0" + hour
    var min = a.getMinutes();
    if (min < 10)
      min = "0" + min
    var sec = a.getSeconds();
    if (sec < 10)
      sec = "0" + sec
      //HUOM otetaan vain tunnit koska haetaan tuntien arvot
    var time = date + '.' + month + '.' + year + ' ' + hour + ':' + min + ':' + sec;
    //var time = hour;
    return time;
  }

  const getFieldData = (data, field) => {

    const _data = []
    data.content.map(data => {

      let value = data[field[0]]
      let name = timeConverter(data[field[1]])

      //console.log(name);

      if (field[1] === 'time') {
        //     console.log(timeConverter(data[field[1]]))
      }

      _data.push(
        {
          "name": name,
          "value": value
        }

      )

    })

    setChartData(_data)
    console.log(JSON.stringify(_data))

  }



  const items = ['temperature', 'pressure', 'humidity', 'windspeed', 'ozone', 'all']

  return (
    <div>

    {/*}  // vois kai tää olla komponentti!!!!*/}
      <Menu color='grey' inverted>
        {items.map(c => (
          <Menu.Item
            key={c}
            name={c}
            color='red'
            active={activeItem === c}
            onClick={handleItemClick}
          />
        ))}
      </Menu>

      <Chart data={chartData}></Chart>
    </div>
  )
}

export default ChartPage