DEPLOY FRONT-END FOR TESTING

+) deploy step:
Step 1: Install XAMPP for window (Choose latest version). Link: https://www.apachefriends.org/download.html

Step 2: Copy "front-end" folder and bring it in C:\xampp\htdocs (based on what disk you install xampp)

Step 3: Access "http://localhost/front-end/home.html?uid=123" in your browser and wait a second for page to load api. 
        Note: uid=123 in this URL is the uid of MCU and it will be set as "123" for testing purpose. 
- You can test other features such as change condition, view all data send from MCU by click button in home page.

+) some api can be used for testing(i asume its uid equal 123): 
-Send data from mcu to server: https://iot-assignment.herokuapp.com/send?temporary=28&humidity=60&mcu=123 
-Change the condition for mcu: https://iot-assignment.herokuapp.com/updatemcu/123?temporary=40&humidity=50
-Get the condition of mcu: https://iot-assignment.herokuapp.com/mcu/123
-Get latest data of mcu that it send to server: https://iot-assignment.herokuapp.com/getone?mcu=123 (if not have any data, so the data may be deleted, you can create data manually by the first api)
-Get all data of mcu that it send to server: https://iot-assignment.herokuapp.com/getall?mcu=123
