#version 330

smooth in vec4 theColor;

smooth vec4 outputColor;

void main()
{
   // Just pass along the input color from the vertex-shader
   outputColor = theColor;
}