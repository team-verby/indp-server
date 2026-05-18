DEPLOY_PATH=/home/ubuntu/app/deploy
LOG_PATH=$DEPLOY_PATH/prod
APP_LOG=$LOG_PATH/application.log
ERROR_LOG=$LOG_PATH/deploy-error.log
DEPLOY_LOG=$LOG_PATH/deploy.log

echo "[ $(date +%c) ] dev 배포 시작" >> $DEPLOY_LOG

CURRENT_PID=$(lsof -ti tcp:8080)
if [ -n "$CURRENT_PID" ]; then
  echo "[ $(date +%c) ] 기존 프로세스 종료: $CURRENT_PID" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

JAR_NAME=$(ls -tr $DEPLOY_PATH | grep jar | tail -n 1)
echo "[ $(date +%c) ] JAR 실행: $JAR_NAME" >> $DEPLOY_LOG
nohup java -jar $DEPLOY_PATH/$JAR_NAME --spring.profiles.active=dev >> $APP_LOG 2>> $ERROR_LOG &

echo "[ $(date +%c) ] dev 배포 완료" >> $DEPLOY_LOG
