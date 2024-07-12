# Event-sourced-RT-Balance-Calculator
This project is an attempt to develop an innovative real-time balance calculation model using event-sourced data. With no need for preliminary data, we dynamically process the presented data to calculate balances on the fly. Join me in this exciting journey of transforming data into actionable insights with cutting-edge techniques!

I have created a transaction service that handles three endpoints

- GET *ping*: returns the current server time
- PUT *load/{messageId}*: credits the given amount to the UserId mentioned
- PUT *authorization/{messageId}*: goes through the overall transactions of the user and verifies if the user can be debited said amount. If yes, commits the transaction.

Also the code returns Error messages for the following situations:

- for illegal or bad requests, if the endpoint is accessed with a bad object, responds with an error message.
- for situations where the entity could not be saved to the database, or when connection to the database failed, responds with an error message.

## Bootstrap instructions
*Replace this: To run this server locally, do the following:*
Prerequisites:
1) Make sure the system you run it on has Java 17 installed. This project is running on Java 17.
2) I used intellij to work on the project. Use any IDE that supports Java and it should work.
3) Does not need any server, I have implemented the application using Spring boot and it uses the embedded tomcat server in Spring. Application endpoints can be accessed by *"localhost:8080/{endpoints}"*
4) Make sure Maven is installed and before running the application, using a terminal, navigate to the project directory
   and run *"mvn install"* or alteratively open the project on IntelliJ, right click on pom.xml, click on maven and click on build project.
   This will install all the required dependencies.
5) The connection string for MongoDb needs to be provided in the resources/application.properties file. You can provide a connection string to your own Atlas cluster, the database and documents will be created automatically.
6) I have used Postman to call and test the endpoints as there is no frontEnd for the application. *Note: Currently working on the front end for the application.*

## Design considerations

- The solution is built using Spring Boot for its robust annotation support, comprehensive dependency injection capabilities, and seamless integration with MongoDB. I have employed the Lombok library to reduce boilerplate code, ensuring a cleaner and more maintainable codebase. Constructors were explicitly defined where necessary.
- Initially, I considered using an H2 Database for its ease of bootstrapping. However, to ensure data persistence beyond program execution and to leverage the flexibility of an unstructured data storage model, I opted for MongoDB. This decision allowed me to couple the amount, currency, and debit/credit status together, avoiding the need for separate tables or storing the amount object as a string in a structured database.
- The solution is designed based on an event-sourcing approach, where all data stored in the database is derived from events (API calls) made to the application. Consequently, all data is recorded in the transactions table.
- The response for all API calls is driven by the service.yml file and will consistently return the current balance of the user. If the request object is incorrectly formatted or if a load request is made with a DEBIT flag (or vice versa), an error message is returned. Authorization requests that do not debit the user’s account will still show the current balance with a response code of DECLINED.
- For monetary calculations, I used the BigDecimal data type and utilized ENUMS for response codes and different currencies, currently supporting USD, EUR, and INR. Negative money values trigger an error, but the transaction is still logged in the database as DECLINED.
- Transactions are managed based on their respective currencies. For example, consider the following sequence of calls:

    - Load 1000 USD 
    - Load 1000 INR 
    - Authorize 500 EUR (DECLINED)
    - Authorize 500 INR (APPROVED)
- The system will report a balance of 1000 USD, 500 INR, and 0 EUR.

To facilitate verification, I have added the following endpoints:

GET /transaction/all: Retrieves all transactions stored in the database.
GET /transaction/{userId}: Retrieves all transactions for a specific user.
These endpoints provide visibility into the stored transactions, ensuring transparency and aiding in validation.

## Deployment considerations
If I were to deploy this application, considering that I'm using Spring Boot and MongoDB, I would also take these deployment considerations into account:
- Utilize Spring Boot Actuator for monitoring and managing application endpoints.
- Implement MongoDB backup and recovery strategies using tools like mongodump and mongorestore.
- Leverage Spring Data MongoDB for simplified database interactions.
- Ensure secure communication between my Spring Boot application and MongoDB using SSL/TLS certificates.
- Use Docker for containerization to package both Spring Boot and MongoDB for easier deployment and scalability.
- Consider MongoDB replica sets for high availability and data redundancy in production.
- Optimize application properties for MongoDB connection pooling and performance tuning.

## Congratulations on making this far into the project description, here's your reward:
```
An ASCII rendition of The Scream - Edvard Munch (not my creation)
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXP
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXY?"""  .
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXY?""   ,;ciCCC
"?YXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXYYY??""   .,;iCCCCCCCCCC
.,.  `""""""???YYYYYYYYYYXXXXXXXYYYYY???"""""""   ..,;ciiCCCCCCCCCCC''`
```'`CCiiicccccccccc;;,,.    .,..,..,;cccciiiCCCCCCC????>''`````   .;ciCCC
Cic;,.   `''<<????CCCCCCCCCCCC?????''''''''`````'   ..,;;;ccciiiCCCCCCCCCC
CCCCCCCCCCCCcc;;;,,.       .,..,..,..,;;cciiiiCCCCCCCCCCCCCCCCCCCCC??>'`
``''?CCCCCCCCCC"'```''CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC??""''``   _.,;cciCC
ic;;,. ````'<?Ciiccc;,,.  ````'''`CCCCCC''''''''`' .,;cciiiCCCCCCCCCC?"'`
CCCCCCCCCiic;._  ```''"<?CCC;;;,,,.,..,...,;;;;;;C777???CC'''''``'  _,xiXX
.  ```'''`CCCCCCCiicc;,,,,,..        ```````'            .,,,xiiXXXXXXXXXX
XXXXXxXx,,,.   ```````````````````   .,..,..,.xiXiiXiiXXXXXXXXXXXXXXXXXXXX
XXXXXXXXXXXXXXXXXXxXxxXxXXXXXxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXY??"""
"""?YXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXYY???"""
=          """""???YYXXXXXXXXXXXXXXXXXXXXXXXXXXYYY???""                 ,;
 -;ccccc;;,.,..            """"""""""                          ,;ciCC'``'
;;, ```'???CCCCC  `''--;CCicc;;,      .,;;,,,.   ..      .,;ciCCCCCCCicc;-
C'```CCiiiiiCCCCtCiicc;,.  ```' . .,;;iCCCCC'' .,;;cccc-''`CCCCCCCCCCCCC;;
CCCCc;,,.,;CCCcc,,,,..'' .;CC'`'CCCCCCCCCC'' ``' .,;;;cc===`CC''````````'
.,...```' .C.,.`""""?CCCCC'`CCC''''``'  .,.,;ciCCCCCC..,;cciiCtttCCCCCCCtt
 ```'     `'`CCCCCCCCCCC' . `'       ```' .,. ```CCC''''`CCCCCCCCCCCCC?"''
..                           ``'                            ```'
$$$$$$$$$$$$ccc$$$$$$$$"?hccc=Jcc$$hccccccc$$$$$$$$$$$$$cccccc,,,,,ccc,,..
$$$??hcccci???CCCCCC$$L ,$$$$c $$hcccccJ???LcccccccccJCCC???????CCCC??????
$$F `?$$$$$$$$$$$$$$$$. ,$$"$$.?$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$,. ,$$$$$$$$$$$$$$$$. ,$$ $$h $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$??????????$$
$$$h $$$$$$$$$$$$$$$$$$ $$$ ?$h ?$$$$$$$$$$$$$$$$$$$$$$$???izc?????????i??
???" ?????$$$$$$$""""""  "" ?"" `"?"""$$$$$$$$??<Lr??cr?=""    .  .      .
,,,,,,,,,J$$$$$$$$,.,,,,,,,,..       ,$$$$$$P>JP"       .,;;,.!!;,.!!!!!!!
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$C3$$  -'  --''`!!!!'`'   ..  `!
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Ci??c,,,.,..,.            `````
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$JJCCCC????????$$????rrrcccc,
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$C<<$$$$$PF<$$$$
$$$$$cizccCCCCCCCCCcccc$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$?????)>>J$CLccc$??""
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$????ii?iiJJJ$$??"""
$$$$$$$$$$$$$??$$$$????P$$$???iiiiicccccc<<????)Cicc$P""      ..   .,;;!!!
$$$$$CCCCCCC>>J>>>>cccccc>>>??C????CC>cccJ$??"""""         -``!!;!'  .!!!'
$$$$$??CCCCCCCCCCCff>>>>>J$$$P""""""""            ..,;;;;;;;!'`.,;;!'''
??????????????"""""'' `'              .,..,;;;;!!!'```..```' .,.,;;;- `,;'
                    .,.    ,;;----'''''''```````'  `''`,;;!!'''`..,;;'' ,;
---;;;;;;;-----'''''''''``'  --- `'  .,,ccc$$hcccccc,.  `' ,;;!!!'``,;;!!'
;;;;,,.,;-------''''''' ,;;!!-    .zJ$$$$$$$$$$$$$$$$$$$c,. `' ,;;!!!!' ,;
  ```'    -;;;!'''''-  `.,..   .zJ$$$$$$$$$$$$$$$$$$$$$$$$$$c, `!!'' ,;!!'
!!-  ' `,;;;;;;;;;;'''''```' ,c$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$c,  ;!!'' ,;
,;;;!!!!!!!!''``.,;;;;!'`'  z$$$$$$$$???"""""'.,,.`"?$$$$$$$$$$$  ``,;;!!!
;;..       --''```_..,;;!  J$$$$$$??,zcd$$$$$$$$$$$$$$$$$$$$$$$$h  ``'``'
```'''   ,;;''``.,.,;;,  ,$$$$$$F,z$$$$$$$$$$$$$$$$$$$c,`""?$$$$$h
!!!!;;;;,   --`!'''''''  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$h.`"$$$$h .
`'''``.,;;;!;;;--;;   zF,$$$$$$$$$$?????$$$$$$$$$$$$$?????$$r ;?$$$ $.
!;.,..,.````.,;;;;  ,$P'J"$$$$$$P" .,c,,.J$$$$$$$$$"',cc,_`?h.`$$$$ $L
'``````'    .,..  ,$$". $ $$$$P",c$$$$$$$$$$$$$$$$',$$$$$$$$$$ $$$$ $$c,
!!!!!!!!!!!!!'''  J$',$ $.`$$P c$$$$$$$$$$$$$$$$$$,$$$$$$$$$$$ $$$$ $$$$C
   ``            J$ ,$P $$ ?$',$$$$???$$$$$$$$$$$$$$$??"""?$$$ <$$$ $$$$$
c           ;,  z$F,$$  `$$ $ ?$"      "$$$.?$$$ $$$P c??c, ?$.<$$',$$$$$F
$$h.  -!>   ('  $" $F ,F ?$ $ F ,="?$$c,`$$F $$"z$$',$' ,$$P $h.`$ ?$$$$$r
$$$$$hc,. ``'  J$ $P J$ . $$F L ",,J$$$F <$hc$$ "$L,`??????,J$$$.` z$$$$$
$$$$$$$$$$c,'' ?F,$',$F.: $$ c$c,,,,,c,,J$$$$$$$ ?$$$c,,,c$$$$$$F. $$$$$$
`"$$$$$$$$$$$c, $$',$$ :: $$$$$$$$F"',$$$$$$$$$$h ?$$$L;;$$$??$$$$ $$$$$$
   "?$$$$$$$$$$ $$$$$$ : .`F"$$$$$$$$$$$$""""?"""h $$$$$$$"$,J$$$$ $$$$$'
      "?$$$$$$$ $$$$$$.`.` h `$$$$$$$$$$$cccc$$c,zJ$$$$$P' $$$$$P',$$$$P
$.       `""?$$ $$$$$$$  ` "$c "?$$$$$$$$$$$$??$$$$$$$$" ,J$$$P",J$$$$P
..           `" ?$$$$$$h    ?$$c.`?$$$$$$$$$' . <$$$$$' ,$$$"  ,$$$$$"
!!>. .          `$$$$$$$h  . "$$$c,"$$$$$$$' `' `$$$P  ,$$$' ,c$$$$$'   ;!
```<!!!>         `$$$$$$$c     "$$$c`?$$$$$  : : $$$  ,$$P' z$$$$$$'   ;!!
$hc ```'  ;       `$$$$$$$.      ?$$c ?$$$$ .: : $$$  $$F ,J$$$$$$'   ;!!
.,..      '        `$$$$$$$       "$$h`$$$$ .' ' $$$ ,$$ ,J$$$$$$'    !!!
????P               `$$$$$$L       $$$ $$$F :.: J$$P J$F J$$$$$P     ;!!
-=<                  ?$$."$$       `$$ ?$$' `' z$$$F $P  $$$$$$'     !!'
cc                   `$$$c`?        ?$.`$$hc, cd$$F ,$'  $$$$$$     ;!!
                      $$$$c         `$$c$$$$$$$$$",c$'   $$$$$$     `!!
                      $$$$$          `?$$$$$$$$$$$$P'    $$$$$$> ..
                      $$$$$            `"?$$$$$$$P"      $$$$$$L $$c,
          !!         <$$$$$            zc,`"""',         <$$$$$$.`$$$$cc,
          !!         J$$$$P            `$$$$$$$' !'       $$$$$$L `$$$$$$h
         ;,          $$$$$L          `! J$$$$$',!!        $$$$$$$  `$$$$$$
          '         <$$$$$.           ! $$$$$$ !!         ?$$$$$$   `$$$$$
                   ,$$$$$$$c          `,`???? ;'         c,?$$$$'    `?$$$
                   $$$$$$$??           `!;;;;!     .     `h."?$P      `$$$
                  ,$$$$$$$h.            `'''      `'      `$$$P        `?$
                   $$$$$$$$h                      `!'      `"'           `
                  `$$$$$$$$F          !;     !    ;,
                   `$$$$$$$'         `!!>         `!
c,        ;,        `?$$$$P           !!>             .
$F        !!>         `""'            `!!            ;!>    <-
$F       `!!'                      ;!; '   `!        <!>    ;
$F        `'      <!               !!!               !!>    !!
?'       `'      !!!               !!!               !!>    !!
         !!'    <!!               ;!!!               `'     ;
        ;!!     !!                !!!!                      !'
        !!!     `'                !!!                       '            ;
        !!                       ;!!'                                    !
                                 !!!                      ;!             !
                                <!!!                      )'            `!
          ,;;>                 ;!!!                                     `!
          `''                 ;!!!                     !                `!
              ;!             ;!!!                                  ,$$c, `
            !''             ;!!!           '                    ,c$$$$$$c.
>                       ;   !!!                                 ?$$$$$$$$$
!!>                   ;!! .!!!     .!>                           "?$$$$$$$
<! `!         ,;     ;!!  !!!!     !!                              `"?$$$$
 . '          '    ;!!! .!!!!     !!   .                              `"?$
 `'               <!!' .!!!!!!   !!!'  !                     >           `
                .!!!  <!!'`!!! .!!!!;                   !!>
                !!!  <!!'  !! ;!!!!!!                   (' ;,
               <!!  !!!'  !!! !!!'!!!                   !> `!
               !!' !!!'  `!!';!>  !!                 <! `' `!  !>.
               ' ;<!!'  .!!! !!' <!'       ;        `!! ;  `!  !!!>
            .<!>;!!!'   !!! `!! <!!                .. ' '      !!!' ;,
           <!!! <!! ;   !!! !!>;!!''!             J$$c         `!!; !!>
          ;!!! ;!! <!   !!> !! `!! !'            J$$$$hr        `'' !!!,;;
          ;!!! !! <!!  <!!  !' ;!! '            <$$$$$$$.           <!!!'!
          !!!  !;<!!'  !!! ;!  !!>              $$$$$$$$$$.          `'  !
         `!!! !!!!!'   !!! !! `!!!              ?$$$$$$$??$c        !!>;
         ;!! ;!!!!!   ;!!> !! <!!>               ?$$$$$$c,`$$.      `!!!
         !!! !!! !'   `!!> !! !!!                 "?$$$$$$ "?$c      `<!
        ;!!  !! ;!    !!!> ! ;!!!,                  "$$$$$$c,"?$c,
        ;!!  !! ;!    !!!! ! `!!!!                    "$$$$$$c ?$$h.
        !!!> !! !!    !!!!    !!!                       "?$$$$c "$$$c,
        !!!' '  !!    `!!!    `!                          "$$$$h.`?$$$c,
       <!!!>   <!!    `!!!     !>                          ?$$$$$c ?$$$$h.
       `!!!    `!!     !!!     `'                           "?$$$$h.`?$$$$
        `!!>    !!     `!!                                    `?$$$$$$$$$$
         `!'    !!      `'                                      "$$$$$$$$$
                `!>                                               ?$$$$$$$
                 `!                                                `"?$$$$
                  `-                ;!                                `"$$
                                                                        `?

------------------------------------------------

The great wave off Kanagawa by Hokusai (not my creation either)
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                      ▒▒  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                      ░░  ▒▒▒▒░░  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                          ░░▓▓▒▒░░▒▒░░░░░░  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                          ░░▒▒  ░░  ░░▒▒▒▒▒▒    ░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒▒▒▒▒░░░░░░░░░░░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                ░░▒▒    ░░░░░░▓▓▒▒▒▒░░  ░░░░░░░░▒▒░░▒▒▒▒░░░░░░░░░░░░░░░░░░░░▒▒▒▒░░░░░░░░░░░░░░░░▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                ▒▒░░░░░░  ░░░░▒▒▓▓░░▒▒▒▒░░░░░░▒▒░░░░    ░░░░░░░░░░░░▒▒░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                    ░░▓▓▓▓▒▒░░░░▒▒▒▒▓▓░░░░░░  ▒▒  ░░░░░░░░▒▒░░░░░░░░▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░              ░░░░▒▒░░░░▓▓▒▒██▒▒░░██▒▒██▓▓░░░░    ░░▒▒░░░░░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                  ░░░░  ░░░░░░██▒▒▒▒▓▓████▓▓██▒▒    ▒▒░░        ░░  ░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░                  ▓▓░░▓▓▓▓▓▓██░░████░░██░░██████▓▓▓▓  ░░▒▒░░▒▒░░░░░░░░  ░░  ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░                  ░░▓▓░░██▓▓▓▓▓▓▒▒████████████████▓▓██░░░░▓▓░░▒▒▒▒  ░░░░  ░░░░▒▒  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░▒▒░░                      ▓▓██████▓▓██████▒▒██████████████░░▓▓▓▓▓▓▒▒░░  ░░░░░░░░  ░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░▒▒▒▒░░                      ▓▓▓▓████████▓▓████████████▒▒████████▒▒  ░░  ░░    ░░  ░░░░░░  ░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░▒▒▒▒░░                          ████░░████████░░██▓▓██████████████▓▓▒▒▒▒░░░░░░  ░░░░      ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒▒▒░░▒▒▒▒░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░
░░░░░░░░░░░░▒▒▒▒▒▒░░                                  ▒▒▓▓▒▒████████▓▓██████████▓▓░░░░▒▒░░▒▒  ░░░░░░░░░░░░░░  ▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
░░░░░░░░░░▒▒▒▒▒▒░░                                  ░░▒▒░░▓▓██████████████████████▓▓░░░░░░░░░░░░░░░░░░░░░░  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▓▓▒▒░░░░░░░░░░░░░░░░▒▒
░░░░▒▒▒▒▒▒▒▒▒▒          ░░░░░░░░          ▓▓▓▓░░        ▓▓██░░▓▓▒▒██████▒▒██████▓▓░░░░░░░░░░░░░░░░░░░░░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░▒▒▒▒▒▒▒▒░░          ░░▓▓▒▒▒▒▒▒          ░░▒▒▒▒░░▒▒▒▒  ░░░░  ▒▒▓▓██████████████▒▒░░░░░░░░░░░░░░░░░░░░░░░░▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
▒▒▒▒▒▒▒▒░░            ░░░░▒▒▒▒      ▒▒        ▒▒▒▒░░░░        ░░▒▒▒▒░░▒▒████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
▒▒▒▒▒▒        ░░      ░░▓▓▓▓▒▒▓▓    ░░▓▓    ░░▓▓▓▓▒▒░░▒▒  ▓▓    ░░░░░░▓▓████▓▓██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
      ░░░░  ░░▒▒░░    ░░▒▒░░  ▒▒▒▒░░  ▒▒  ░░  ░░  ░░░░▓▓▒▒▒▒░░    ░░██░░████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    ░░▓▓▒▒▒▒░░▒▒░░▓▓██▓▓░░░░  ▒▒▒▒▒▒  ░░░░▒▒▒▒░░  ▒▒░░██░░  ░░  ▒▒▓▓██▓▓████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    ▒▒▓▓░░▒▒▒▒▒▒▒▒▓▓░░▒▒██▒▒░░▒▒▒▒▒▒  ░░▓▓▒▒████▓▓▒▒▓▓▓▓▒▒▓▓▒▒    ░░████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░  
    ░░  ░░░░▒▒░░░░▒▒▓▓██▓▓████▒▒░░▒▒░░  ▒▒▒▒████████████▓▓██▒▒██▓▓▒▒██████████▓▓▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░    
  ░░░░▒▒░░▒▒░░▒▒░░░░  ▒▒██████▓▓  ░░▒▒▒▒░░░░▒▒  ▓▓████████████▓▓▓▓██████▒▒██▒▒██▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░      
  ░░░░░░▒▒▒▒▒▒▒▒░░▒▒▓▓░░▒▒████▒▒░░▒▒  ▒▒▒▒▓▓░░  ████████▒▒████▒▒████████████▓▓▒▒██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░      
░░▓▓▒▒░░▒▒░░██▓▓▓▓░░░░▓▓██▓▓██▓▓  ▓▓░░▒▒▓▓▒▒▒▒░░░░▒▒░░████████████░░░░██████████▓▓▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░      ░░
░░░░░░██▒▒▓▓████████▒▒██████░░░░▒▒▒▒▒▒▒▒▒▒  ░░▒▒▓▓░░██████▒▒████░░      ░░▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░      ░░░░
░░▓▓▓▓▓▓██░░▓▓████▓▓▒▒▓▓██████▓▓▒▒▓▓░░▓▓██▒▒  ▒▒░░▓▓████████████                ▓▓██▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒▒▒▒▒░░    ░░▒▒▒▒
  ░░▓▓▓▓██░░  ▒▒▓▓██████████▒▒  ████▒▒██████▓▓▓▓░░▓▓▓▓████████░░                  ████▓▓▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▒▒░░░░░░▒▒░░░░░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓░░▒▒░░    ░░░░░░▓▓
  ▒▒░░░░    ▒▒░░▒▒██▒▒████████████▒▒██████████░░▒▒▒▒▓▓██▓▓▒▒▒▒                      ▓▓██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▒▒░░      ▒▒▓▓░░▒▒
░░  ▓▓▒▒░░▒▒▒▒░░░░▒▒████▓▓████▓▓██████████▓▓████▓▓████▓▓██▒▒                        ▒▒▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒      ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░▓▓▓▓██░░          ░░▓▓▓▓
▒▒▒▒▒▒░░▒▒░░▓▓  ░░▒▒▒▒▓▓▓▓████████████████▓▓██████▓▓▓▓░░▓▓              ▒▒░░          ▓▓██▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒          ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▓▓██  ░░    ▒▒░░▒▒▒▒▒▒▓▓▓▓
▒▒░░░░▒▒░░▒▒▓▓░░░░░░▒▒▒▒▒▒████████▓▓████▓▓██▓▓████████░░░░          ░░  ░░▒▒          ░░████▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒              ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓██▓▓░░▒▒░░░░    ░░▓▓▒▒▓▓▓▓▓▓██
▒▒▒▒▒▒▒▒░░░░░░░░▒▒░░▒▒▒▒▒▒▒▒██████████████████████████▒▒              ░░░░            ▒▒▓▓████▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒    ░░  ░░        ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓  ▒▒░░░░░░    ░░░░▒▒▒▒▓▓▓▓▓▓██
▒▒▓▓████░░██░░▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▓▓██████████████▓▓  ░░            ▒▒░░░░              ▓▓░░  ▒▒██▓▓▒▒▒▒▒▒▒▒▒▒      ██▒▒░░░░  ░░    ░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░▒▒░░░░    ░░▒▒░░▓▓▒▒▓▓▓▓▓▓▓▓
▒▒▓▓████▓▓▓▓░░██▒▒▒▒░░▒▒▒▒▒▒░░░░░░░░▒▒▓▓██▒▒██░░                  ░░▒▒▓▓░░  ░░        ░░▒▒          ▒▒▒▒▒▒  ░░▒▒▓▓██▓▓▒▒▓▓▒▒░░▒▒      ▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░  ░░▒▒░░▒▒▒▒▒▒▒▒▓▓▓▓▓▓▓▓▓▓
▒▒▓▓▓▓▓▓██░░████▒▒▒▒▒▒░░▒▒▒▒▒▒░░░░░░░░░░░░▒▒░░                  ░░  ░░  ░░░░▓▓░░        ░░░░          ░░▒▒██░░██▒▒▒▒██████████░░▓▓▒▒  ░░░░▒▒▒▒░░░░░░░░    ░░░░░░▓▓▒▒▒▒▓▓▓▓▓▓▓▓▓▓░░  
  ░░▒▒████▓▓██▓▓██▒▒▒▒▒▒▒▒▒▒████▒▒▓▓██▒▒░░              ▒▒░░▒▒    ░░░░▓▓░░▓▓▒▒▓▓          ░░░░          ▓▓████████▓▓░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░░░░░        ░░▓▓▒▒▓▓▓▓▓▓▒▒▓▓▓▓▒▒░░▒▒░░
      ░░░░▓▓▓▓████▓▓▒▒▒▒▒▒░░▒▒▓▓░░░░                    ░░▓▓▓▓  ▒▒▒▒  ▓▓▒▒▒▒▒▒▒▒░░▒▒░░    ░░      ░░▒▒▒▒░░░░██████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░░░░░▒▒░░    ░░  ░░░░▒▒▓▓▓▓▓▓▓▓████░░  ░░▓▓▓▓  
            ░░▓▓▒▒████▒▒░░            ░░▒▒▒▒▒▒▒▒    ░░  ▒▒▒▒    ░░▒▒░░▓▓▒▒▒▒▒▒▓▓░░  ▒▒    ▒▒  ░░▒▒  ░░▒▒▒▒░░  ░░▓▓▓▓▓▓▓▓▓▓░░░░░░    ░░        ░░▒▒▒▒░░░░▓▓▓▓██▓▓▓▓░░░░  ▒▒▒▒▓▓██  ░░
                                      ░░▒▒▒▒▒▒▒▒▒▒  ▒▒░░    ░░▒▒▒▒▒▒▒▒▒▒▓▓▒▒▒▒░░░░░░▒▒▓▓  ▒▒▒▒▓▓░░▒▒    ▒▒        ░░░░              ░░▒▒▒▒▒▒▒▒▓▓▒▒▓▓▓▓▒▒▒▒      ░░▒▒▒▒▒▒▓▓▒▒▓▓▒▒  ▓▓
            ░░░░░░          ░░▓▓░░        ▒▒▒▒▒▒▒▒  ▒▒▒▒▓▓▒▒░░  ▒▒▒▒▒▒▒▒▒▒░░░░  ▓▓░░▒▒▒▒▒▒▒▒██░░░░░░  ░░▓▓▒▒    ░░██▓▓░░      ▒▒░░░░            ▒▒            ▒▒▒▒▓▓▓▓▒▒▓▓██▓▓  ▓▓▓▓
      ▒▒▓▓▒▒▒▒▒▒▒▒▒▒    ░░▓▓▒▒▒▒▒▒░░    ▒▒░░▒▒▒▒▓▓  ░░▒▒▒▒▒▒▒▒▒▒▒▒▓▓▒▒▒▒▒▒▓▓▒▒▒▒▒▒▒▒▓▓▒▒░░░░██▓▓  ▓▓░░▒▒▒▒▓▓▒▒  ░░▓▓████░░    ▒▒▒▒▒▒▒▒                ░░▒▒      ░░▒▒▓▓██▓▓██░░▒▒▒▒▓▓
    ░░▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ░░░░▒▒▓▓▓▓    ▒▒░░▓▓▒▒░░▓▓▓▓░░  ▒▒▒▒▓▓▒▒▒▒▓▓▒▒▓▓  ▒▒▒▒▒▒▓▓▒▒░░▒▒▓▓░░▓▓██░░▒▒░░▓▓▓▓▒▒▓▓▓▓  ▓▓▓▓████▓▓▒▒░░▓▓▓▓▒▒▒▒  ▓▓▒▒░░      ▓▓▓▓▓▓▒▒░░  ▒▒░░░░██░░░░▒▒▓▓▒▒
    ░░▒▒▒▒▒▒░░░░▒▒▒▒▓▓░░▒▒▒▒▒▒▓▓░░    ▒▒▒▒▓▓▒▒░░░░▒▒▓▓  ░░▒▒    ░░  ▒▒    ░░▓▓▒▒▒▒  ░░▓▓▒▒▒▒▒▒██▓▓░░▒▒░░▒▒▓▓  ░░▓▓▒▒██▓▓████▓▓▒▒▒▒████▓▓▒▒  ▒▒▓▓▒▒░░    ▒▒██▓▓▓▓██▒▒▓▓██▒▒    ▓▓▒▒▓▓
    ░░▒▒▒▒        ▒▒▓▓▒▒▒▒▒▒▒▒▓▓██▓▓    ▓▓░░░░▒▒▓▓▓▓▒▒░░        ▒▒▒▒░░      ░░▓▓░░  ░░▒▒▒▒▒▒▓▓░░██▒▒▓▓██▒▒▓▓▓▓  ░░██████████████▒▒██████▓▓▒▒  ▒▒▒▒▒▒▓▓░░▒▒░░▓▓██▓▓▓▓████░░    ▒▒██▓▓
            ▒▒▒▒░░▒▒░░▒▒▒▒▒▒▒▒░░████▒▒░░▓▓░░  ▒▒▒▒░░    ▓▓░░    ░░░░▓▓░░▒▒░░▓▓▒▒░░  ░░██▓▓████▒▒▒▒████▓▓████▓▓▒▒    ▒▒████████████▓▓██████▓▓▓▓░░▒▒▓▓▒▒▓▓▓▓▓▓▒▒▒▒▓▓▓▓▓▓░░    ▓▓▓▓████
      ░░▓▓████▓▓▒▒░░▓▓▓▓▓▓▓▓▓▓▓▓████▓▓▓▓▓▓▓▓▒▒▒▒▒▒░░  ▒▒▒▒    ░░▓▓▒▒▓▓░░▒▒▒▒▓▓▓▓▒▒    ██▓▓██████░░▒▒▓▓▓▓▓▓████▓▓▓▓▓▓▓▓░░████████████▓▓██████▓▓▒▒▒▒▒▒██▓▓▓▓▓▓██▓▓▒▒██░░    ▒▒▒▒▒▒▓▓▓▓
      ██░░██████▓▓▓▓▓▓██████████████▒▒░░▒▒▒▒▒▒░░░░▒▒  ░░▒▒    ░░  ▒▒░░▒▒▒▒▒▒▒▒  ░░▒▒  ▒▒██▓▓████▓▓░░▒▒████████▓▓▓▓▓▓▓▓▓▓▒▒██████████████████████▓▓▒▒░░▓▓████▓▓████░░    ▒▒▒▒▓▓██████
    ▒▒██████▓▓▓▓▓▓░░██▒▒▓▓▓▓▓▓▒▒▒▒░░  ░░▒▒▓▓░░▒▒▒▒▒▒░░    ░░▒▒▒▒  ▓▓░░▒▒░░  ░░    ▒▒    ██▓▓██████▒▒░░░░▒▒▓▓▓▓▓▓██▓▓▓▓▓▓▓▓▒▒▓▓▓▓██████████████████▓▓░░██▓▓▓▓▓▓▓▓░░    ▓▓▓▓████▓▓▓▓  
    ▓▓████▓▓▒▒░░▓▓██▓▓████▒▒▒▒    ▒▒▓▓▒▒▒▒██▒▒░░▒▒▒▒▒▒░░      ░░░░▒▒▒▒██░░▓▓▒▒▓▓  ▓▓░░    ██▒▒██████▒▒░░▒▒▒▒▓▓▒▒░░▒▒▒▒▓▓▓▓▓▓▓▓▒▒▓▓██████████████▒▒████▓▓▓▓▒▒░░░░    ▒▒▒▒░░  ▒▒  ▒▒██
    ████▒▒██▒▒▓▓██████████████▒▒  ▒▒████▓▓▒▒██▓▓  ▒▒▒▒░░▓▓▒▒▒▒▒▒▓▓▒▒▒▒████▓▓██▓▓▒▒▒▒░░      ██▒▒▓▓████░░░░▒▒░░▒▒▒▒▒▒▒▒▒▒▒▒░░▒▒▒▒▒▒▒▒▓▓████░░░░██░░▒▒▒▒░░██▓▓░░  ▒▒▒▒▓▓▓▓██████████▓▓
    ▒▒▒▒██▓▓██████▓▓████▒▒██▓▓▒▒████████▒▒▓▓██▓▓▓▓░░▒▒▓▓▒▒▒▒▓▓▒▒▒▒▓▓░░██░░████░░    ▓▓▒▒▓▓    ████▓▓████░░░░░░░░▒▒░░░░░░░░░░░░░░░░░░░░░░▒▒▒▒░░▓▓▒▒▒▒▓▓▒▒░░░░▓▓▓▓▓▓██▓▓▓▓████████████
    ░░██░░▓▓▒▒██▓▓████▓▓████████████▓▓████▓▓██████▓▓  ▒▒▒▒  ▒▒░░▒▒▒▒  ▒▒████░░██▓▓▒▒░░▒▒▒▒▒▒    ▒▒████████▒▒░░░░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░▒▒░░        ░░▒▒▓▓████████▓▓▓▓▓▓▓▓  
      ▓▓██████████▓▓████████████████▓▓██████▒▒████▒▒▓▓  ▒▒▒▒██░░    ▒▒░░  ████▒▒██░░░░▒▒▓▓░░░░      ██████████▒▒▒▒░░░░▒▒▒▒▒▒░░▒▒░░▒▒░░░░░░░░░░  ░░░░░░▓▓▓▓▒▒░░░░  ░░▒▒▒▒▒▒      ░░░░
░░    ░░████▓▓██████████████████████████▓▓██████▓▓▒▒▓▓  ░░▓▓██▒▒▒▒▒▒████▒▒░░▓▓██▓▓▒▒▒▒██▓▓▒▒▒▒▓▓░░      ▒▒▒▒████████████████████▒▒░░░░        ░░▓▓▒▒▒▒▒▒▒▒▓▓▒▒▓▓▒▒▒▒░░░░░░▒▒▒▒▓▓▒▒▒▒
  ░░    ▒▒████████████▓▓████▓▓████▒▒██████▒▒████████  ▓▓▓▓  ▒▒▒▒░░▓▓██░░▒▒▒▒▒▒████░░██████▒▒▒▒▒▒▒▒▒▒░░          ░░▒▒▒▒▒▒░░              ░░▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
