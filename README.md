# Flash-Light
#This repo is written in JAVA for an Android mobile application 
(A)	 FlashLight Lite

Prepare at least the basis functionality and the layout at home. You can use a memory stick to transport your project from home
to the lab. Use the eclipse export feature. Please mind, that the minimum SDK should be 8 (2.2) and you MUST use the same
API-Version as installed in the virtual machine disk image you got for your exercises and lab assignments. First thing in the lab is to
import your projects into the lab computers running that same virtual mas-chine disk image. Other API/SDK Versions bring you more
trouble than you probably can handle. So use the virtual machine to test your assignment.
1. Build a small android flashlight app with the following functionality:
_ There is a large colored background which is indeed a Textview containing a
short description what you can do with this app.
_ There are buttons on the bottom of the screen for black, white,red,yellow and
green which
_ change the background color accordingly (see Color.rgb())
_ Use one callback and a switch to decide which button was pressed using getId().
_ To let the Textview expand into the remaining space on the screen use the
“weight” attribute.
2. Extend the app with a toggle event. With each tap on the Textview the buttons
appear/disappear and the Textview extends to the space of the buttons (see weight):
_ Use an anonymous class “OnClickListener()” which is created in your Activity
class and
_ use setVisibility() to change the visibility of the layout the buttons are in.
3. Let the Textview text disappear after the first toggle event.
4. Let the buttons hide automatically after one button was pressed.
5. Change the screen orientation (Ctrl-F11 or keypad 7 in the emulator). What
happens to your color setting, your hidden buttons and the removed text? Add the
required code to save your instance’s state to your activity in onSaveInstanceState()
and of course restore that state in onCreate()..


(B)	FlashLight Option Button Hide
Add another menu option to configure the startup color the app will show first. You need the follow this steps:
_ Create a new file “radiogroup.xml” within .../res/layout with a LinearLayout as
top item (vertically oriented) and let this contain a RadioGroup with the colors as
RadioButton items.
_ Use the filename part “radiogroup” w/o extension as reference name in R.Layout:
View toDisplayInDialog = getLayoutInflater().inflate(R.layout.radiogroup, null);
to create a inflated view of your definition in “radiogroup.xml” and
_ connect it to to your AlertDialog with setView(toDisplayInDialog);
_ You find your id’s now via
toDisplayInDialog.findViewById()
since the RadioGroup and radioButtons are not part of your activity’s layout but
part of your newly inflated View.
_ To find the index (not the id!) of a sequence of RadioButtons you can use
RadioGroup myRadioGroup = (RadioGroup)
toDisplayInDialog.findViewById(R.id.radioGroup);
int radioGroupId = myRadioGroup.getCheckedRadioButtonId;
RadioButton myCheckedButton = (RadioButton)
toDisplayInDialog.findViewById(radioGroupId();
int index = myRadioGroup.indexOfChild(myCheckedButton);
If your colors are ordered the same way the RadioButton items are, you can use the
index directly.
_ Add positive/negative buttons to that AlertDialog configuring your preferences.
_ What happens, if you have a screen to small for your RadioGroup and on an
orientation change to your Dialog window? Repair this. Does the startup color
show up also after a restart of your app in then radiogroup as checked?


(C) FlashLight Advanced with Option AsyncTask

_ Add a new button “SOS” which will start a Thread (AsyncTask) sending
"...---..." in morse code by changing the background color of the screen.
Avoid also blinking with the same color....
Stop this thread if another button is pressed or the screen is touched. Mind
also that with orientation changes you would contact to your AsyncTask instance
which need to be stopped before that happens. Since the call sequence is
onPause()/onSaveInstanceState()/onStop() you better do the stopping business in
onStop().
_ Find a way in your program to stop your screen from switching off after a while.
Hint, there’s a FLAG for that which you can apply to the window.

(D) FlashLight Advanced Power Management

 This part is entirely optional!
_ restructure your app to use to use an Enum class for the colors.
_ Add initialization to show the correct radiobutton item for the color saved in your
app’s preferences. Access the nth item by
RadioButton myRadioButton = (RadioButton)
myRadioGroup.getChildAt(myRadioButtonIndex);
myRadioButton.setChecked(true);
_ Add a broadcast listener for battery events and if the battery level falls below a
certain point you will remove the FLAG keep screen on.
_ Add long dark pauses into the SOS to save some power. For this you need to stop
and restart AsyncTask because we do not want it to run in the background any
more and we need to start the morse code sequence anyway from the beginning
after we turn on again.
This is at best accomplished by using the Handler interface which allows to send
time-delayed messages. A battery low message would reset the keep screen on
FLAG and use the PowerManager to get a wakelock via ’newWakeLock()’ (flags
FULL_WAKE_LOCK, ACQUIRE_CAUSES_WAKEUP). Your app needs now the permission
WAKE_LOCK. If you use the acquire() your device stay on or switches on and if you
release() your device goes off after the screen-timeout has elapsed. To get rid of the
keyguard you must set or clear FLAG_DISMISS_KEYGUARD and SHOW_WHEN_LOCKED.
So the sequence of messages would be:
– Battery-LOW: switch mode and let device go off, send delayed Device-ON
message
– Device-ON: acquire() wakelock and send delayed Device-OFF message
– Device-OFF: release() wakelock and send delayed Device-ON message
– Battery-HIGH: remove pending messages and go back into normal mode.
