module.exports = function (req,res) {
    console.log(`serving url:${req.path} with method ${req.method}`)
    if(req.path === '/api/user/login'){
        res.status(200).end();
    }else{
        res.end();
    }
}
