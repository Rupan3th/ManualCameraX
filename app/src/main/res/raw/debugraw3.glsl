#version 300 es
precision mediump float;
precision mediump usampler2D;
uniform usampler2D InputBuffer;
uniform int yOffset;

out vec4 Output;

void main() {
    ivec2 xy = ivec2(gl_FragCoord.xy);
    xy+=ivec2(0,yOffset);
    uint inp = texelFetch(InputBuffer,xy,0).x;
    Output = vec4(float(inp)/65535.0);
}