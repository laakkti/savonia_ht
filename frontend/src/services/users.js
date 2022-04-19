import axios from 'axios'
const baseUrl = '/api/users'

const users = async (newUser) => {

  const response = await axios.post(baseUrl, newUser)
  return response.data
};

export default { users }