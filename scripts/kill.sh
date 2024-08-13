PROJECT_ROOT=/home/ubuntu/app
DEPLOY_PATH=$PROJECT_ROOT/deploy
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

IDLE_PORT=$1

for retry_count in $(seq 1 10)
do
  IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

  if [ -z "$IDLE_PID" ]
  then
    echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션이 종료됨" >> $DEPLOY_LOG
    break
  else
    echo "[ $(date +%c) ] kill -15 $IDLE_PID" >> $DEPLOY_LOG
    kill -15 "$IDLE_PID"
    sleep 5
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "[ $(date +%c) ] $IDLE_PORT kill 실패." >> $DEPLOY_LOG
    exit 1
  fi

  echo "[ $(date +%c) ] $IDLE_PORT 포트에서 구동 중인 애플리케이션 종료 실패. 재시도..." >> $DEPLOY_LOG
done
