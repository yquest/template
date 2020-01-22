module.exports = function(req, res) {
  console.log(`serving state initialisation, page:${req.url}`);

      var car1 ={
        make: 3,
        model: "2005",
        maturityDate: new Date(2020, 0, 1, 10, 0, 0, 0).getTime(),
        price: 10000
      }

  var state1Test = {
    username: "xico",
    auth: false,
    cars: [ car1 ]
  };

  var emptyCars={
    //page:"login",
    username: "xico",
    auth: false,
    cars: []
  };
  res.writeHead(200, { "Content-Type": "application/javascript" });
  res.end(`__state = ${JSON.stringify(state1Test)};`);
};
