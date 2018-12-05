backend i have already deploy in heroku page. 
and we can use immediately those apis:

+) some api can be used for testing(i asume its uid equal 123): 
-Send data from mcu to server: https://iot-assignment.herokuapp.com/send?temporary=28&humidity=60&mcu=123 
-Change the condition for mcu: https://iot-assignment.herokuapp.com/updatemcu/123?temporary=40&humidity=50
-Get the condition of mcu: https://iot-assignment.herokuapp.com/mcu/123
-Get latest data of mcu that it send to server: https://iot-assignment.herokuapp.com/getone?mcu=123 (if not have any data, so the data may be deleted, you can create data manually by the first api)
-Get all data of mcu that it send to server: https://iot-assignment.herokuapp.com/getall?mcu=123 (it will limit 10 latest data)
