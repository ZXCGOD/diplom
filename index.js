const SocketServer = require('websocket').server
const http = require('http')
const mysql = require('mysql2');
var ip = require("ip");
console.dir ( ip.address() );
var connectionToDB = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'diplom'
});

 

 


const server = http.createServer((req, res) => {})

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []

wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')
    connections.push(connection)

    connection.on('message', (mes) => {

        var mydata = JSON.parse(mes.utf8Data);
        console.log(mydata);
        if(mydata.purpose == "registration"){

            connectionToDB.query(
              'INSERT INTO users VALUES(null,' +  mydata.email + ' , ' +  mydata.name + ' , ' + mydata.password + ', "")',
              function(err, results, fields) {
                
               }
            )
        } else if(mydata.purpose == "deleteUserFromChat"){

            connectionToDB.query(
              'DELETE FROM user_in_chat where id_user = ' +  mydata.id + ' ;',
              function(err, results, fields) {
                
               }
            )
        }
        else if(mydata.purpose == "authentication")
        {
            connectionToDB.query(
              'SELECT * FROM users WHERE email = "' + mydata.email + '" AND password = "' + mydata.password + '"',
              function(err, results, fields) {
                if(results.length>0){
                    console.log("User присутствует");

                    let row = "{ ok:'true', id: '" + results[0].id + "', name : '" + results[0].name + "', email: '"+ results[0].email +"' , photo: '"+ results[0].photo +"' }";
                      console.log(row);
                      connection.sendUTF(
                        row
      );

                }   else {
                     connection.sendUTF(
                        "{ ok:'false'}"
                        );
                }
            }
               
            );
        } else if(mydata.purpose == "getListOfChats"){
             connectionToDB.query(
              'SELECT * FROM chats WHERE  id in( SELECT id_chat FROM user_in_chat where id_user = ' + mydata.id + ');',
              function(err, results, fields) {
                
                    results.forEach(function(element,index){
                        
                         let row = "{ id: '" + element.id + "', name : '" + element.name + "'}";
                   
                    console.log(row);

                    connection.sendUTF(
                        row
                    );

                    });

                   

                 
            }
               
            );
        }else if(mydata.purpose == "getListOfUsersInChat"){
             connectionToDB.query(
              'select users.id,users.name,users.email from users,user_in_chat where user_in_chat.id_chat = ' + mydata.id + ' and user_in_chat.id_user = users.id;',
              function(err, results, fields) {
                
                    results.forEach(function(element,index){
                        
                         let row = "{ id: '" + element.id + "', name : '" + element.name + "', email : '"+element.email+"'}";
                   
                    console.log(row);

                    connection.sendUTF(
                        row
                    );

                    });

                   

                 
            }
               
            );
        }else if(mydata.purpose == "message"){

        connectionToDB.query(
              'INSERT INTO messages VALUES(null,' +  mydata.id_chat + ' , ' +  mydata.id_user + ' , "' + mydata.message + '")',
              function(err, results, fields) {
                
               }
            )

        connections.forEach(element => {
            if (element != connection){
                element.sendUTF(mes.utf8Data)
            }   else {
               
            }

        })}
        else if(mydata.purpose == "getListOfMessages"){

        connectionToDB.query(
              'SELECT messages.id,id_chat,id_user,users.name,message FROM messages,users where id_chat ='+mydata.id_chat+' and messages.id_user = users.id order by messages.id;',
              function(err, results, fields) {

                  results.forEach(function(element,index){
                        
                         let row = "{id: '" + element.id + "', id_chat : '" + element.id_chat + "', id_user : '" + element.id_user + "' , name_user : '" + element.name + "', message : '" + element.message + "'}";
                   
                    console.log(row);

                    connection.sendUTF(
                        row
                    );

                    });

                
               }
            )

        connections.forEach(element => {
            if (element != connection){
                element.sendUTF(mes.utf8Data)
            }   else {
               
            }

        })}

       
    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        connections.splice(connections.indexOf(connection), 1)
    })

})