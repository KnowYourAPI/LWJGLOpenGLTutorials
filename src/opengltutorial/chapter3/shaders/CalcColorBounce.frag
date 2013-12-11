#version 330

out vec4 outputColor;

uniform float fragLoopDuration;
uniform float time;

const vec4 firstColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
const vec4 secondColor = vec4(0.0f, 1.0f, 0.0f, 1.0f);

// Additional shader for further study
// Rather than popping from secondColor to firstColor
// the color now "bounces" back

void main()
{
	float currTime = mod(time, fragLoopDuration);
	float currLerp = 0;

	if(currTime > fragLoopDuration / 4) {
		currLerp = 0.5f - currTime / fragLoopDuration;
	} else {
		currLerp = currTime / fragLoopDuration;
	}
	
	
	outputColor = mix(firstColor, secondColor, currLerp * 4);
}
