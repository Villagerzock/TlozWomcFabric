#version 150

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec4 texColor = texture(Sampler0, texCoord0);
    vec3 lightColor = vec3(0.0, 1.0, 1.0); // Türkises Licht
    fragColor = texColor * vec4(lightColor, 1.0) * vertexColor;
}