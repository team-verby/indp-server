PROJECT_ROOT=/home/ubuntu/app
DEPLOY_PATH=$PROJECT_ROOT/deploy
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

IDLE_PORT=$1

for retry_count in $(seq 1 10)
do
  response=$(curl -s http://127.0.0.1:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then
      echo "[ $(date +%c) ] Health check 성공" >> $DEPLOY_LOG
      break
  else
      echo "[ $(date +%c) ] Health check의 응답을 알 수 없거나 혹은 status가 UP이 아님" >> $DEPLOY_LOG
      echo "[ $(date +%c) ] Health check: ${response}" >> $DEPLOY_LOG
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "[ $(date +%c) ] Health check 실패." >> $DEPLOY_LOG
    echo "[ $(date +%c) ] Nginx에 연결하지 않고 배포 종료" >> $DEPLOY_LOG
    exit 1
  fi

  echo "[ $(date +%c) ] Health check 연결 실패. 재시도..." >> $DEPLOY_LOG
  sleep 10
done
