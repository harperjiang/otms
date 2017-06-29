otms.namespace('otms.helpPage');

otms.helpPage.topicRoot = {
	'topic' : '帮助中心',
	"children" : [ {
		"id" : 1,
		"topic" : "课程相关",
		"children" : [ {
			'id' : 100,
			'topic' : '开始上课之前',
			'end' : true,
			'children' : [ {
				'id' : 10001,
				'topic' : '我的孩子能听懂外教说话吗？'
			} ]
		}, {
			"id" : 101,
			'topic' : '试听课',
			'end' : true,
			'children' : [ {
				"id" : 10101,
				'topic' : '如何预约试听课？'
			}, {
				"id" : 10102,
				'topic' : '如何查看已预约的试听课？'
			}, {
				"id" : 10103,
				'topic' : '我可以取消预约的试听课吗？'
			}, {
				"id" : 10104,
				'topic' : '我错过了试听课的时间怎么办？'
			} ]
		}, {
			"id" : 102,
			'topic' : '正式课程',
			'end' : true,
			'children' : [ {
				"id" : 10201,
				'topic' : '如何查看已预约的课程？',
			}, {
				"id" : 10202,
				'topic' : '我可以修改预约的课程时间吗？',
			}, {
				"id" : 10203,
				'topic' : '我错过了课程时间怎么办？',
			}, {
				'id' : 10204,
				'topic' : '教师没有出现怎么办？'
			} ]
		} ]
	}, {
		'id' : 4,
		'topic' : '沟通与反馈',
		'children' : [ {
			'id' : 401,
			'topic' : '与教师沟通',
			'end' : true,
			'children' : [ {
				'id' : 40000,
				'topic' : '我对教学效果不满意怎么办？'
			} ]
		} ]
	}, {
		"id" : 2,
		"topic" : "软件使用",
		"children" : [ {
			'id' : 200,
			'topic' : 'Zoom网络课堂',
			'end' : true,
			'children' : [ {
				'id' : 20001,
				'topic' : '如何安装Zoom？'
			}, {
				'id' : 20002,
				'topic' : '如何启动Zoom？'
			}, {
				'id' : 20003,
				'topic' : '如何使用Zoom？'
			} ]
		} ]
	}, {
		"id" : 3,
		"topic" : "财务相关",
		"children" : [ {
			'id' : 300,
			'topic' : '课时费用',
			'end' : true,
			'children' : [ {
				'id' : 30000,
				'topic' : '私人外教每个月要花多少钱？'
			}, {
				'id' : 30001,
				'topic' : '课时费用是如何计算的？'
			}, {
				'id' : 30002,
				'topic' : '我需要提前支付课时费用吗？'
			}, {
				'id' : 30003,
				'topic' : '我的账户余额不足，可以预约课程吗？'
			}, {
				'id' : 30004,
				'topic' : '我的账户余额不足，可以预约课程吗？'
			}, {
				'id' : 30005,
				'topic' : '我对某一节课程不满意，可以申请退款吗？'
			} ]
		}, {
			'id' : 301,
			'topic' : '付款与提现',
			'end' : true,
			'children' : [ {
				'id' : 30101,
				'topic' : '支持的付款方式有哪些？'
			}, {
				'id' : 30102,
				'topic' : '为什么要通过乐教营支付？'
			}, {
				'id' : 30103,
				'topic' : '我的账户资金安全吗？'
			}, {
				'id' : 30104,
				'topic' : '我能把账户余额提现吗？'
			} ]
		} ]
	} ]
};

otms.helpPage.popularTopic = [];