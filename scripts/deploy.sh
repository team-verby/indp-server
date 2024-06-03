PROJECT_ROOT=/home/ubuntu/app
PROJECT_NAME=indp-server

DEPLOY_PATH=$PROJECT_ROOT/deploy

APP_LOG=$DEPLOY_PATH/application.log
ERROR_LOG=$DEPLOY_PATH/deploy-error.log
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

mkdir $DEPLOY_PATH

echo "------------------------------------------------------------------------------------" >> $DEPLOY_LOG

echo "[ $(date +%c) ] Build 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/$PROJECT_NAME/build/libs/*.jar $DEPLOY_PATH/

CURRENT_PROFILE=$(curl -s http://localhost/api/profile)
echo "[ $(date +%c) ] 현재 구동 중인 Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG
if [ "$CURRENT_PROFILE" == prod1 ]
then
  IDLE_PROFILE=prod2
  IDLE_PORT=8082
elif [ "$CURRENT_PROFILE" == prod2 ]
then
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG
  echo "> prod1을 할당합니다. IDLE_PROFILE: prod1" >> $DEPLOY_LOG
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
fi

echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션 pid 확인" >> $DEPLOY_LOG
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z "$IDLE_PID" ]
then
  echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션이 없으므로 종료하지 않습니다." >> $DEPLOY_LOG
else
  echo "[ $(date +%c) ] kill -15 $IDLE_PID" >> $DEPLOY_LOG
  kill -15 "$IDLE_PID"
  sleep 5
fi

echo "[ $(date +%c) ] 새 어플리케이션 배포" >> $DEPLOY_LOG

JAR_NAME=$(ls -tr "$DEPLOY_PATH" | grep jar | tail -n 1)

echo "[ $(date +%c) ] JAR Name: $JAR_NAME" >> $DEPLOY_LOG
nohup java -jar $DEPLOY_PATH/"$JAR_NAME" --spring.profiles.active=$IDLE_PROFILE > $APP_LOG 2> $ERROR_LOG &

echo "[ $(date +%c) ] $IDLE_PROFILE 10초 후 Health check 시작" >> $DEPLOY_LOG
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then
      echo "[ $(date +%c) ] Health check 성공" >> $DEPLOY_LOG
      break
  else
      echo "[ $(date +%c) ] Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다." >> $DEPLOY_LOG
      echo "[ $(date +%c) ] Health check: ${response}" >> $DEPLOY_LOG
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "[ $(date +%c) ] Health check 실패. " >> $DEPLOY_LOG
    echo "[ $(date +%c) ] Nginx에 연결하지 않고 배포를 종료합니다." >> $DEPLOY_LOG
    exit 1
  fi

  echo "[ $(date +%c) ] Health check 연결 실패. 재시도..." >> $DEPLOY_LOG
  sleep 10
done

echo "[ $(date +%c) ] Port Switching" >> $DEPLOY_LOG
sleep 10
sudo sh $PROJECT_ROOT/indp-server/scripts/switch.sh
