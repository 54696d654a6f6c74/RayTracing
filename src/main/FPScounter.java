package main;

public class FPScounter
{
    final Main main;

    FPScounter(Main main)
    {
        this.main = main;
    }

    int timePassed;
    int timePassedLast = 0;

    float fps;

    void update()
    {
        timePassed = main.millis();
        fps = 1000f/((float)(timePassed-timePassedLast));
        timePassedLast = timePassed;
    }

    void show()
    {
        update();

        main.fill(255);
        main.textSize(20);
        main.text(fps, 0, 20);
    }
}
