class FPScounter
{
    int timePassed;
    int timePassedLast = 0;

    float fps;

    void update()
    {
        timePassed = millis();
        fps = 1000.0/((float)(timePassed-timePassedLast));
        timePassedLast = timePassed;
    }

    void show()
    {
        update();

        println(fps);

        fill(255);
        textSize(20);
        text(fps, 0, 20);
    }
}
