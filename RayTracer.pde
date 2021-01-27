Boundry[] walls;
LightSource source;
FPScounter fps;

void setup()
{  
    size(1280, 720);
    walls = new Boundry[100];
    for(int i = 0; i < walls.length; i++)
    walls[i] = new Boundry();

    source = new LightSource(0, 0, walls);
    fps = new FPScounter();
}

int lastMillis = 0;

void draw()
{
    background(0);

    fps.show();

    source.move(mouseX, mouseY);
    source.castLight();
}
