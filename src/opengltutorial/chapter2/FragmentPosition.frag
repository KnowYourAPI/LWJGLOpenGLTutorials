#version 330

out vec4 outputColor;
void main()
{
   // Fragment-height divided by window height
   // lerpValue has to be between 0 and 1 for the in the
   // mix function
   float lerpValue = gl_FragCoord.y / 500.f;
   
   // Interpolate white with a 'less white' white
   outputColor = mix(vec4(1.0f, 1.0f, 1.0f, 1.0f),
   					 vec4(0.2f, 0.2f, 0.2f, 1.0f),
   					 lerpValue);
}