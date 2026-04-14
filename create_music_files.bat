@echo off
echo Creating silent audio files for testing...

cd /d "c:\Users\admin\IdeaProjects\TikTokAffiliateTool\src\main\resources\static"

REM Create 12-second luxury background music (silent)
C:\ffmpeg\ffmpeg.exe -f lavfi -i "anullsrc=r=44100:cl=stereo" -t 12 -c:a aac -b:a 128k -y luxury_bgm.mp3

REM Create 12-second chill vibes music (silent)  
C:\ffmpeg\ffmpeg.exe -f lavfi -i "anullsrc=r=44100:cl=stereo" -t 12 -c:a aac -b:a 128k -y chill_vibes.mp3

REM Create 7-second fast dynamic music (silent)
C:\ffmpeg\ffmpeg.exe -f lavfi -i "anullsrc=r=44100:cl=stereo" -t 7 -c:a aac -b:a 128k -y fast_dynamic.mp3

echo Done! Created 3 silent audio files for testing.
pause