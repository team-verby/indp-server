PROJECT_ROOT=/home/ubuntu/app
DEPLOY_PATH=$PROJECT_ROOT/deploy
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

IDLE_PORT=$1

for retry_count in $(seq 1 20)
do
  response=$(curl -s http://127.0.0.1:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then
      echo "[ $(date +%c) ] Health check 성공 (${retry_count}회차)" >> $DEPLOY_LOG
      break
  else
      echo "[ $(date +%c) ] Health check 미응답 또는 UP 아님 (${retry_count}/20)" >> $DEPLOY_LOG
      echo "[ $(date +%c) ] Health check: ${response}" >> $DEPLOY_LOG
  fi

  if [ $retry_count -eq 20 ]
  then
    echo "[ $(date +%c) ] Health check 실패 — 최대 재시도 횟수 초과." >> $DEPLOY_LOG
    echo "[ $(date +%c) ] Nginx에 연결하지 않고 배포 종료" >> $DEPLOY_LOG
    exit 1
  fi

  echo "[ $(date +%c) ] Health check 재시도..." >> $DEPLOY_LOG
  sleep 15
done
