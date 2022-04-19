import axios from 'axios'
const baseUrl = '/api/datas'

//let token = null


const setToken = newToken => {
  const token = `bearer ${newToken}`
  return token
}


/*
const queryAddress = () => {

  const request = axios.get('/info' + "/queryAddress")
  //const request = axios.get('http://localhost:3001/address') //'' + "/address")

  return request.then(response => response.data)
  
}
*/

const queryReady = (_socketId) => {
  console.log("queryRedy on front " + _socketId)
  const request = axios.get(baseUrl + "/queryReady", {  
    params: {
      socketId: _socketId
    }
  })

  return request.then(response => response.data)
}

const getTitles = (user) => {
  const request = axios.get(baseUrl + "/titles", {
    params: {
      user: user
    }
  })

  return request.then(response => response.data)
}

// ei tarvita useria koska id sisöltää uniikin avaimen
// ohjautuu routeriin :id joten parametrit turhia

const getItem = (id) => {

  console.log("in getItemn datas.js id= " + baseUrl + "/" + id);
  const request = axios.get(baseUrl + "/" + id, {
    params: {
      itemId: id
    }
  })
  return request.then(response => response.data)
}

const deleteItem = (id) => {

  console.log("in deleteItemn datas.js id= " + baseUrl + "/" + id);
  const request = axios.delete(baseUrl + "/" + id, {
    params: {
      itemId: id
    }
  })
  return request.then(response => response.data)
}



export default { getTitles, getItem,deleteItem,setToken, queryReady}
