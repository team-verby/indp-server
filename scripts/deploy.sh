PROJECT_ROOT=/home/ubuntu/app
PROJECT_NAME=indp-server
APPLICATION_NAME=indp

DEPLOY_PATH=$PROJECT_ROOT/deploy

APP_LOG=$DEPLOY_PATH/application.log
ERROR_LOG=$DEPLOY_PATH/deploy-error.log
DEPLOY_LOG=$DEPLOY_PATH/deploy.log

mkdir $DEPLOY_PATH

echo "✅ Build 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/$PROJECT_NAME/build/libs/*.jar $DEPLOY_PATH/

echo "✅ 현재 구동 중인 애플리케이션 pid 확인" >> $DEPLOY_LOG
CURRENT_PID=$(pgrep -f $APPLICATION_NAME.*.jar)

echo "✅ 현재 구동 중인 애플리케이션 pid: $CURRENT_PID" >> $DEPLOY_LOG
if [ -z "$CURRENT_PID" ]; then
        echo "✅ 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다." >> $DEPLOY_LOG
else
        echo "✅ kill -15 $CURRENT_PID" >> $DEPLOY_LOG
        kill -15 "$CURRENT_PID"
        sleep 5
fi

echo "✅ 새 어플리케이션 배포" >> $DEPLOY_LOG

JAR_NAME=$(ls -tr "$DEPLOY_PATH" | grep jar | tail -n 1)

echo "✅ JAR Name: $JAR_NAME" >> $DEPLOY_LOG
nohup java -jar $DEPLOY_PATH/$JAR_NAME --spring.profiles.active=dev > $APP_LOG 2> $ERROR_LOG &
