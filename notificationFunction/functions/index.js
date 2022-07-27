'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{user_id}/{newNotificationId}')
.onWrite((data, context) =>
{
const user_id = context.params.user_id;
const newNotificationId = context.params.newNotificationId;


   console.log('We have a notification from : ', user_id);
   
   if(!data.after.val())
   {
	      console.log('A notification has been deleted  : ', newNotificationId);
    return null;
   }
const DeviceToken = admin.database().ref((`/Users/${user_id}/device_token`).once('value');
  return DeviceToken.then(result => 
  {
      const token_id = result.val();
	  
	  
      const payload = 
	  {
	      notification:
		  {
		  title: "New chat request",
		  body: `you have a new chat request, please check`,
		  icon: "default"
		  }
	  
	  };
	  
	   return admin.messaging().sendToDevice(token_id, payload)
	   .then(response =>
	   {
		   console.log('This was the notification Feature');
	   })
  });
});
