module.exports = function(req, res) {
  console.log(`serving state initialisation, page:${req.url}`);

  function createDate(year, month, days, hours, minutes, seconds){
    return new Date(year, month-1, days, hours, minutes, seconds, 0).getTime();
  }

  const car1 = {
    make: 3,
    model: "2005",
    maturityDate: createDate(2020,1,2,3,4,5),
    price: 10000
  };

  const state1Test = {
    username: "xico",
    auth: false,
    cars: [car1]
  };

  const emptyCars = {
    //page:"login",
    username: "xico",
    auth: false,
    cars: []
  };
  res.writeHead(200, { "Content-Type": "application/javascript" });
  res.end(`__state = ${JSON.stringify(state1Test)};`);
};
