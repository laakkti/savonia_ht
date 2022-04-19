import axios from 'axios'
const baseUrl = '/api/devices'

const createNew = async (newDevice) => {

  const response = await axios.post(baseUrl, newDevice)
  return response.data
}

const getAll = () => {
  const request = axios.get(baseUrl)

  return request.then(response => response.data)
}

export default { createNew, getAll }