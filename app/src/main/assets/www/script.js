window.onload = function () {
      if ( typeof(Android) !== "undefined" && Android !== null) {
        Android.readyFunction();
      };

}

var resizeTimer;


var username = "";
var password = "";
var numberCall = "";



var checkTab=0;
var statusCallFlow = 0;


var isregister = false;
var countRetry = 350;
function accept(){

     if(session == null)
        return;

     var remote = document.querySelector('video#remote');
     var local = document.querySelector('video#local');
     var options = mediaOptions(true, false, remote, local);
     session.accept(options);
     console.log("not null");

}


var timeStart=-1;
var timeEnd=-1;
var typeCall = {"null":-1,"incall":0, "outcall":1, "misscall":2};
var typeCallIndex=typeCall.null;


var config = {
  userAgentString: 'SIP.js/Hackathon 2016 HCM Android',
  traceSip: true,
  register: true,
  registerExpires: 1800,
  session_timers: false,

   wsServers: "ws://118.69.135.152:7080",
   server: "118.69.135.152",
   stunServers: "stun:118.69.135.152:3478",
   hackIpInContact: true
};


function logout(){

         if(session != null){
                session.terminate();
                delete session;
                session = null;
          }

        if(ua != null){
           ua.unregister();
           delete ua;
           ua = null;
        }

}
function hidden()
{
          if(ua != null){
           ua.unregister();
           }
}

function mediaOptions(audio, video, remoteRender, localRender) {
    return {
        media: {
            constraints: {
                audio: audio,
                video: video
            },
            render: {
                remote: {
                    video: remoteRender
                },
                local: {
                    video: localRender
                }
            }
        }
    };
}

function randomKey(){
		return Math.floor((Math.random() * 1000) + 1); 
	}

var ua = null;//UserAgent
var session;

function login(user, pass){

        console.log("login from android..." + user + " " + pass);
    	config['displayName'] = user;
    	config['uri'] = user + "@" + config.server;
    	config['authorizationUser'] = user;
    	config['password'] = pass;
    	console.log("createUA() call from Adnroid");
    	ua = new SIP.UA(config);
    	handleUAEventsAndroid();
    	ua.start();


}
function updateSatusCallFlow()
{
    statusCallFlow = 0;
}
function registertoserver(user, pass,token){
    config['userAgentString'] = token;
    if(ua!=null)
    {
        ua.register();
    }

}



function makeCall(number_call){
    if(username == '' || !registered){
         alert("Bạn cần đăng nhập lại");

         window.location="index.html";

         return;
    }


	if(number_call!=username){
    numberCall = number_call;
    console.log("makeCall() numberCall: " + numberCall);
    
    var remote = document.querySelector('video#remote');
    var local = document.querySelector('video#local');
	var options = mediaOptions(true, false, remote, local);
	var target = numberCall + "@" + config.server;
    
	console.log("target " + target);
    console.log("ua " + ua);
    
    setupSession(ua.invite("sip:" + target, options));
    incoming = false;
	//showOutcomingPage();

    //enable audio

     if ( typeof(Android) !== "undefined" && Android !== null) {
            Android.playAudio();
     }

	}else{
		
        alert("Not call by yourself!");
	}
	}


var timeCall=0;
function endSession () {
	checkCalled =false;
    console.log("endSession () ");
    removeMedia();//clear screen
    console.log("endSession2222222222222 () ");
    if (session) {
        delete session;
        session = null;
        console.log("endSession111111111111111 () ");
    }
}

function setupSession (s) {

    isOnTerminated = false;
    session = s;
    console.log("session: " + session);

    session.on('accepted', onAccepted);    
    session.on('cancel', cancelSession);
    session.on('bye', byeSession);
    session.on('failed', failedSession);

     session.on('rejected', function (response, cause){
    	console.log("session: rejected " + cause);

    	onTerminated(cause);
      });

     session.on('terminated', function (message, cause) {

        console.log("session: terminated " + cause);

        //Android.onTerminated(cause);
        statusCallFlow = 0;
        onTerminated(cause);
      });
    session.on('progress', function (response) {
            console.log("session: terminated " + response.status_code);
            Android.onProgress(response.status_code);

          });
       session.on('ended', function () {
        	console.log("session: ended");
          onTerminated("");
       });
}

function cancelSession(){
    console.log("cancelSession ()");
     delete session;
     session = null;
    Android.onCancel();

}

function byeSession(request){
    console.log("byeSession ()");
    delete session;
    session = null;
    Android.onBye();

}

var failed = false;
function failedSession (response, cause) {
    console.log("failedSession () cause:" + cause);
    failed = true;

    Android.failedSession(cause);

}

var isOnTerminated = false;
function onTerminated (cause) {

   if(isOnTerminated)
       return;

    Android.onTerminated(cause);

   if ( typeof(Android) !== "undefined" && Android !== null) {
       Android.pauseAudio();
   }

   isOnTerminated = true;
   console.log("onTerminated ()"+cause);
   endSession();
}

function onAccepted () {
	
		   console.log('%c incoming', 'background: #222; color: #bada55');
	//typeCallIndex=typeCall.incoming;

    console.log("onAccepted () ");

    var remoteStreams = session.getRemoteStreams();

    SIP.WebRTC.MediaStreamManager.render(remoteStreams, document.querySelector('video#remote'));
    

    Android.onAccepted();
    //statusCallFlow = 2;
}

var countResize = 0;


function attachMediaStream (element, stream) {
console.log('attachMediaStream() element ' + element);
    
  if (typeof element.src !== 'undefined') {
    URL.revokeObjectURL(element.src);
    element.src = URL.createObjectURL(stream);
      
  } else if (typeof element.srcObject !== 'undefined'
       || typeof element.mozSrcObject !== 'undefined') {
    element.srcObject = element.mozSrcObject = stream;
      
  } else {
    console.log('Error attaching stream to element.');
    return false;
  }

  //ensureMediaPlaying(element);
  return true;
}


function removeMedia(){
    document.querySelector('video#remote').pause();
    document.querySelector('video#local').pause();
    document.querySelector('video#remote').src = null;
    document.querySelector('video#local').src = null;
}

var incoming = false;
var registered = false;

var isFistTime = true;



function call(numberCall){
            statusCallFlow = 2;
            console.log("makeCall() numberCall: " + numberCall);

            var remote = document.querySelector('video#remote');
            var local = document.querySelector('video#local');
        	var options = mediaOptions(true, false, remote, local);
        	var target = numberCall + "@" + config.server;

        	  console.log("target " + target);
              console.log(ua);

        setupSession(ua.invite("sip:" + target, options));
        incoming = false;

}

function handleUAEventsAndroid(){

    console.log("handleUAEvents()");

     ua.on('registered', function(e) {
           console.log(e);
           var department = e.getHeader('Department');
           Android.onRegistered(department);
           isregister = true;
           countRetry=350;

     });


     ua.on('unregistered', function(e) {
           console.log("is unregistered");
           Android.onUnregistered();
     });

     ua.on('registrationFailed', function(e) {
          console.log("is registrationFailed");
          //if(!navigator.onLine)
          Android.onRegistrationFailed();
         // ua.stop();
        isregister = false;
       /* if(isregister==false)
        {
            if(countRetry>0)
            {

                //Android.onRegistrationFailed();
                setTimeout(registertoserver,5000);
                countRetry--;
            }
            else{countRetry=350}
        }
*/
     });

	 ua.on('connected', function () {
		 console.log('connect WebSocket server');
		 Android.onConnected();
     });
	ua.on('disconnected', function () {
		 console.log('disconnect WebSocket server');
		 Android.onDisconnected();
	});

    ua.on('cancel', function() {
         console.log('client send cancel');

         Android.onCancel();
         statusCallFlow = 0;

    });

    ua.on('bye', function(request) {
        console.log('client send "bye"');
        Android.onBye();
        statusCallFlow = 0;

    });
    ua.on('progress', function (response) {
        console.log('ua progress');


    });

    ua.on('connecting', function () {
        console.log('ua connecting');
        //Android.onConnecting();
    });

	//a remote invitation is calling
	ua.on('invite', function (incomingsession) {//session = SIP.Session = SIP.ServerContext
		 console.log('an invitation is comming ');

        if(statusCallFlow==0)
          {
		  setupSession(incomingsession);
		  var number = session.remoteIdentity.uri.toString();
		  Android.onInvite(number);
		  statusCallFlow = 1;
		  console.log('an invitation is comming ');
		  }
		  else
		  {
		    incomingsession.reject();
		  }

    });

    ua.on('message', function (msg) {
		console.log("msg.body: " + msg.body);

		Android.onMessage();

	});
  }



 window.addEventListener('online',  updateOnlineStatus);
 window.addEventListener('offline', updateOnlineStatus);
 window.addEventListener('onunload', onDestroy);

function onDestroy(){
    console.log("onDestroy");
     console.log("abc");
    if(session == null)
    {
        console.log("abcd");
        return;
    }
    if (session.startTime && !session.endTime) {
     		session.bye();
     		console.log("abcdef");
     }else

      if(incoming){//is inbound
          try{
              //session.reject();
              session.cancel();
              console.log("1");
          }catch(ex){
              session.terminate();
              console.log("2");
          }
      }else if(session != null){//outbound
         try{
          session.cancel();
          statusCallFlow = 0;
          console.log("3");
        }catch(ex){
          session.reject();
          console.log("4");
        }
      }

    delete session;
    session = null;
}

 function updateOnlineStatus(event){
     var condition = navigator.onLine ? "online" : "offline";

     console.log("beforeend", "Event: " + event.type + "; Status: " + condition);
 }

//send dtmf
function sendDtmf(number){
    session.dtmf(number);
}
