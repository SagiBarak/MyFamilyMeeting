# MyFamilyMeeting
This project made for a specific requests of the customer.<br>
It does all the way from .docx file into the relevant values in the application.

## How does it work?
I've created a fragment for administration. The <a href="https://github.com/SagiBarak/MyFamilyMeeting/blob/master/app/src/main/java/sagib/edu/myfamilymeeting/AdminFragment.java">AdminFragment</a> let the customer upload his .doc/.docx file directly to Firebase Storage and then parse this .docx file (which contains text and images) and create full copy (by values) in Firebase Database.
All this procedure happens by two buttons.. one for upload and one for sync, that's all!<br>
By default, the application shows the latest parsed file.<br>
There is an option to change the data in the application into a previous parsed file.<br>
The data will be cached while the application starts, so there will be no loading while the user is using the app.

## Tools
**Android Studio** - for the whole project.

## Versions
Still in progress... Beta testing with my customer.

## Using libraries
<a href="https://github.com/JakeWharton/butterknife">Butter Knife</a>, 
<a href="https://github.com/square/picasso">Picasso</a>, 
<a href="https://github.com/facebook/fresco">Fresco</a>.
<a href="https://github.com/Bearded-Hen/Android-Bootstrap">Android-Bootstrap</a>, 
<a href="https://github.com/chathuralakmal/AndroidImagePopup">AndroidImagePopup</a>.
