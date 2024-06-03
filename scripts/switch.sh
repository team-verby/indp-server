PROJECT_ROOT=/home/ubuntu/app
DEPLOY_PATH=$PROJECT_ROOT/deploy
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

CURRENT_PROFILE=$(curl -s http://localhost/profile)
echo "[ $(date +%c) ] 현재 구동 중인 Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG
if [ "$CURRENT_PROFILE" == prod1 ]
then
  CURRENT_PORT=8081
  IDLE_PROFILE=prod2
  IDLE_PORT=8082
elif [ "$CURRENT_PROFILE" == prod2 ]
then
  CURRENT_PORT=8082
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG
  echo "> prod1을 할당합니다." >> $DEPLOY_LOG
  IDLE_PROFILE=prod1
  IDLE_PORT=8081
fi

echo "[ $(date +%c) ] Nginx Current Profile / Port: $CURRENT_PROFILE / $CURRENT_PORT" >> $DEPLOY_LOG
echo "[ $(date +%c) ] 전환할 Profile / Port: $IDLE_PROFILE / $IDLE_PORT" >> $DEPLOY_LOG
echo "set \$service_url http://localhost:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

echo "[ $(date +%c) ] Nginx Reload" >> $DEPLOY_LOG
sudo service nginx reload

CURRENT_PROFILE=$(curl -s http://localhost/api/profile)
echo "[ $(date +%c) ] Nginx Current Profile: $CURRENT_PROFILE" >> $DEPLOY_LOG