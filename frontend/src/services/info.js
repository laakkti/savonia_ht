import axios from 'axios'
const baseUrl = '/api/info'


const queryAddress = () => {

    const request = axios.get(baseUrl + "/queryAddress")
    //const request = axios.get(baseUrl + "/addr")

    return request.then(response => response.data)

}

export default { queryAddress }