PROJECT_ROOT=/home/ubuntu/app
PROJECT_NAME=indp-server

SERVER_URL=api.verby.co.kr

DEPLOY_PATH=$PROJECT_ROOT/deploy
SCRIPT_PATH=$PROJECT_ROOT/$PROJECT_NAME/scripts/

APP_LOG=$DEPLOY_PATH/application.log
ERROR_LOG=$DEPLOY_PATH/deploy-error.log
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

echo "\n[ $(date +%c) ] 배포 시작" >> $DEPLOY_LOG

echo "[ $(date +%c) ] Build 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/$PROJECT_NAME/build/libs/*.jar $DEPLOY_PATH/

CURRENT_PROFILE=$(curl -s https://$SERVER_URL/profile)
echo "[ $(date +%c) ] 현재 구동 중인 Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG

if [ $CURRENT_PROFILE = "prod1" ]
then
  IDLE_PROFILE=prod2
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE = "prod2" ]
then
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
else
  echo "[ $(date +%c) ] 일치하는 Profile이 존재하지 않음 Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG
  echo "[ $(date +%c) ] prod1 할당" >> $DEPLOY_LOG
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
fi

echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션 pid 확인" >> $DEPLOY_LOG
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z "$IDLE_PID" ]
then
  echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션이 없으므로 종료하지 않음" >> $DEPLOY_LOG
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
sudo sh $SCRIPT_PATH/health-check.sh $IDLE_PORT

echo "[ $(date +%c) ] Port Switching" >> $DEPLOY_LOG
sleep 10
sudo sh $SCRIPT_PATH/switch.sh
