import React, { PureComponent } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const Chart = ({ data }) => {

    return (
      <LineChart
        width={700}
        height={420}
            data={data}
        margin={{
          top: 5, right: 30, left: 20, bottom: 5,
        }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="value" stroke="#8884d8" activeDot={{ r: 8 }} />
        {/*<Line type="monotone" dataKey="uv" stroke="#82ca9d" />*/}
      </LineChart>
    );
  }

  export default Chart