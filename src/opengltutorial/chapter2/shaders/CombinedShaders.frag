#version 330

smooth in vec4 theColor;

out vec4 outputColor;
void main()
{
   // Fragment-height divided by window height
   // lerpValue has to be between 0 and 1 for the in the
   // mix function
   float lerpValue = gl_FragCoord.y / 500.f;
   
   // Interpolate white with a 'less white' white
   outputColor = mix(theColor,
   					 vec4(0.2f, 0.2f, 0.2f, 0.2f),
   					 lerpValue);
}