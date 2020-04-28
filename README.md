#### Higload server

Thread Pool Java server for static files

Number of threads : thread_limit

#### Build 

`$ sudo docker build . -t alex`

#### Run

`$ sudo docker run -p 8080:8080 -v ~/httpWebServer/etc/httpd.conf:/etc/httpd.conf -v ~/httpWebServer/var/www/html:/var/www/html --name alex -t alex`